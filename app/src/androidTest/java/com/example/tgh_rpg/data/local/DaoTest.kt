package com.example.tgh_rpg.data.local

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

    @Before
    fun criarBanco() {
        val ctx = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(ctx, AppDatabase::class.java).build()
        encontros = db.encontroDao()
        personagens = db.personagemDao()
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
        assertEquals(5, criado!!.vida)   // nasce com 5 vidas
        personagens.atualizar(criado.copy(vida = 0, acertos = 7, status = StatusJogo.MORTO))
        val lido = personagens.buscar(id)!!
        assertEquals(7, lido.acertos)
        assertEquals(StatusJogo.MORTO, lido.status)
    }
}
