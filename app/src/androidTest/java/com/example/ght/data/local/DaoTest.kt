package com.example.ght.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DaoTest {

    private lateinit var db: AppDatabase
    private lateinit var encontros: EncontroDao
    private lateinit var personagens: PersonagemDao
    private lateinit var itens: ItemDao

    @Before
    fun criarBanco() {
        val ctx = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(ctx, AppDatabase::class.java).build()
        encontros = db.encontroDao()
        personagens = db.personagemDao()
        itens = db.itemDao()
    }

    @After
    fun fecharBanco() = db.close()

    private fun encontro(id: String) = EncontroEntity(
        id = id, titulo = "Titulo $id", texto = "texto",
        opcaoA = Opcao(rotulo = "A", atributo = Atributo.AGILIDADE, dificuldade = 12)
    )

    @Test
    fun salvaELeAsOpcoesEmbutidas() = runBlocking<Unit> {
        encontros.salvar(encontro("e1"))
        val lista = encontros.observarTodos().first()
        assertEquals(1, lista.size)
        assertEquals("A", lista[0].opcaoA.rotulo)
        assertEquals(Atributo.AGILIDADE, lista[0].opcaoA.atributo)
        assertEquals(12, lista[0].opcaoA.dificuldade)
    }

    @Test
    fun upsertComMesmoIdAtualizaSemDuplicar() = runBlocking<Unit> {
        encontros.salvar(encontro("e1"))
        encontros.salvar(encontro("e1").copy(titulo = "Novo"))
        val lista = encontros.observarTodos().first()
        assertEquals(1, lista.size)
        assertEquals("Novo", lista[0].titulo)
    }

    @Test
    fun apagarRemoveOEncontro() = runBlocking<Unit> {
        encontros.salvar(encontro("e1"))
        encontros.apagar(encontro("e1"))
        assertEquals(0, encontros.contar())
    }

    @Test
    fun sortearNaoRepeteOExcluido() = runBlocking<Unit> {
        encontros.salvarTodos(listOf(encontro("e1"), encontro("e2")))
        repeat(20) { assertEquals("e2", encontros.sortearUm("e1")?.id) }
    }

    @Test
    fun sortearDevolveNullSeSoExisteOExcluido() = runBlocking<Unit> {
        encontros.salvar(encontro("e1"))
        assertNull(encontros.sortearUm("e1"))
    }

    @Test
    fun substituirTodosComListaVaziaNaoApagaOCache() = runBlocking<Unit> {
        encontros.salvar(encontro("e1"))
        encontros.substituirTodos(emptyList())
        assertEquals(1, encontros.contar())
    }

    @Test
    fun personagemInsereEAtualiza() = runBlocking<Unit> {
        val id = personagens.inserir(PersonagemEntity(nome = "Ana", forca = 5, agilidade = 3, mente = 1))
        val criado = personagens.buscar(id)
        assertNotNull(criado)
        assertEquals(20, criado!!.vida)
        personagens.atualizar(criado.copy(vida = 0, acertos = 7, status = StatusJogo.MORTO))
        val lido = personagens.buscar(id)!!
        assertEquals(7, lido.acertos)
        assertEquals(StatusJogo.MORTO, lido.status)
    }

    // ---- Bug 2: personagem que VENCEU continua sendo o save atual ----

    @Test
    fun personagemQueVenceuContinuaSendoOAtivo() = runBlocking<Unit> {
        val id = personagens.inserir(PersonagemEntity(nome = "Ana"))
        personagens.atualizar(personagens.buscar(id)!!.copy(eventos = 10, status = StatusJogo.VENCEU))
        assertEquals(id, personagens.buscarAtivo()?.id)
        assertEquals(id, personagens.observarAtivo().first()?.id)
    }

    @Test
    fun personagemMortoNaoEhOAtivo() = runBlocking<Unit> {
        val id = personagens.inserir(PersonagemEntity(nome = "Ana"))
        personagens.atualizar(personagens.buscar(id)!!.copy(status = StatusJogo.MORTO))
        assertNull(personagens.buscarAtivo())
    }

    @Test
    fun encerrarAtivosTambemEncerraQuemVenceu() = runBlocking<Unit> {
        val id = personagens.inserir(PersonagemEntity(nome = "Ana", status = StatusJogo.VENCEU))
        personagens.encerrarAtivos()
        assertEquals(StatusJogo.MORTO, personagens.buscar(id)!!.status)
        assertNull(personagens.buscarAtivo())
    }

    // ---- Bug 1: inventário persistido e atrelado ao personagem vivo ----

    @Test
    fun inventarioEhLidoDoBancoParaOPersonagemAtivo() = runBlocking<Unit> {
        val id = personagens.inserir(PersonagemEntity(nome = "Ana"))
        itens.inserir(ItemEntity(personagemId = id, nome = "Espada", efeito = "+1 força", bonusForca = 1))
        val lido = itens.observarDoAtivo().first()
        assertEquals(1, lido.size)
        assertEquals("Espada", lido[0].nome)
        assertEquals(1, lido[0].bonusForca)
    }

    @Test
    fun inventarioContinuaDepoisQueOPersonagemVence() = runBlocking<Unit> {
        val id = personagens.inserir(PersonagemEntity(nome = "Ana"))
        itens.inserir(ItemEntity(personagemId = id, nome = "Espada", efeito = ""))
        personagens.atualizar(personagens.buscar(id)!!.copy(status = StatusJogo.VENCEU))
        assertEquals(1, itens.observarDoAtivo().first().size)
    }

    @Test
    fun inventarioMostraSoOsItensDoPersonagemAtivo() = runBlocking<Unit> {
        val velho = personagens.inserir(PersonagemEntity(nome = "Velho", criadoEm = 1L))
        itens.inserir(ItemEntity(personagemId = velho, nome = "ItemDoVelho", efeito = ""))
        personagens.encerrarAtivos()
        val novo = personagens.inserir(PersonagemEntity(nome = "Novo", criadoEm = 2L))
        itens.inserir(ItemEntity(personagemId = novo, nome = "ItemDoNovo", efeito = ""))
        assertEquals(listOf("ItemDoNovo"), itens.observarDoAtivo().first().map { it.nome })
    }

    @Test
    fun apagarOrfaosRemoveItensDePersonagemMortoOuApagado() = runBlocking<Unit> {
        val morto = personagens.inserir(PersonagemEntity(nome = "Morto"))
        val vivo = personagens.inserir(PersonagemEntity(nome = "Vivo", criadoEm = System.currentTimeMillis() + 1))
        itens.inserir(ItemEntity(personagemId = morto, nome = "A", efeito = ""))
        itens.inserir(ItemEntity(personagemId = vivo, nome = "B", efeito = ""))
        personagens.atualizar(personagens.buscar(morto)!!.copy(status = StatusJogo.MORTO))
        itens.apagarOrfaos()
        assertEquals(0, itens.listarDoPersonagem(morto).size)
        assertEquals(1, itens.listarDoPersonagem(vivo).size)
    }

    @Test
    fun sincronizacaoComReplaceNaoApagaOInventario() = runBlocking<Unit> {
        val id = personagens.inserir(PersonagemEntity(nome = "Ana"))
        itens.inserir(ItemEntity(personagemId = id, nome = "Espada", efeito = ""))
        personagens.salvarSincronizado(personagens.buscar(id)!!.copy(vida = 5))
        assertEquals(1, itens.listarDoPersonagem(id).size)
    }
}
