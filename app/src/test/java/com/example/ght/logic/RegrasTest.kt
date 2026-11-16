package com.example.ght.logic

import com.example.ght.data.local.Atributo
import com.example.ght.data.local.Opcao
import com.example.ght.data.local.StatusJogo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RegrasTest {

    private val guerreiro = novoPersonagem("Ana", Classe.GUERREIRO)

    private fun opcao(atributo: Atributo, dificuldade: Int) = Opcao(
        rotulo = "x", atributo = atributo, dificuldade = dificuldade,
        textoSucesso = "ok", textoFalha = "falhou"
    )

    private val acerto = Resultado(true, "ok", 15, 5, 10)
    private val erro = Resultado(false, "falhou", 2, 5, 10)

    @Test
    fun rolagem20SempreAcerta() {
        assertTrue(resolver(guerreiro, opcao(Atributo.MENTE, 30), 20).sucesso)
    }

    @Test
    fun rolagem1SempreErra() {
        assertFalse(resolver(guerreiro, opcao(Atributo.FORCA, 2), 1).sucesso)
    }

    @Test
    fun somaOAtributoDoPersonagem() {
        assertTrue(resolver(guerreiro, opcao(Atributo.FORCA, 11), 6).sucesso)
        assertFalse(resolver(guerreiro, opcao(Atributo.FORCA, 11), 5).sucesso)
    }

    @Test
    fun erroTiraDuasVidas() {
        val depois = aplicar(guerreiro, erro)
        assertEquals(18, depois.vida)
        assertEquals(0, depois.acertos)
        assertEquals(1, depois.eventos)
    }

    @Test
    fun zerarVidaMataSemRecompensa() {
        var p = guerreiro
        repeat(10) { p = aplicar(p, erro) }
        assertEquals(StatusJogo.MORTO, p.status)
        assertEquals("", p.recompensa)
    }

    @Test
    fun dezAcertosVencemEGanhamARecompensa() {
        var p = guerreiro
        repeat(TOTAL_EVENTOS) { p = aplicar(p, acerto) }
        assertEquals(StatusJogo.VENCEU, p.status)
        assertEquals(RECOMPENSA_FINAL, p.recompensa)
        assertEquals(20, p.vida)
    }
}
