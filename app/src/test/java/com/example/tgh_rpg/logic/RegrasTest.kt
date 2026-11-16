package com.example.tgh_rpg.logic

import com.example.tgh_rpg.data.local.Atributo
import com.example.tgh_rpg.data.local.Opcao
import com.example.tgh_rpg.data.local.StatusJogo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

// Teste de JVM: roda sem emulador (Run no arquivo, ou ./gradlew testDebugUnitTest)
class RegrasTest {

    private val guerreiro = novoPersonagem("Ana", Classe.GUERREIRO)   // força 5, agilidade 3, mente 1

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
        // força 5 + rolagem 6 = 11, que bate a dificuldade 11
        assertTrue(resolver(guerreiro, opcao(Atributo.FORCA, 11), 6).sucesso)
        assertFalse(resolver(guerreiro, opcao(Atributo.FORCA, 11), 5).sucesso)
    }

    @Test
    fun erroTiraUmaVida() {
        val depois = aplicar(guerreiro, erro)
        assertEquals(4, depois.vida)
        assertEquals(0, depois.acertos)
        assertEquals(1, depois.eventos)
    }

    @Test
    fun cincoErrosMatamESemRecompensa() {
        var p = guerreiro
        repeat(5) { p = aplicar(p, erro) }
        assertEquals(StatusJogo.MORTO, p.status)
        assertEquals("", p.recompensa)
    }

    @Test
    fun dezAcertosVencemEGanhamARecompensa() {
        var p = guerreiro
        repeat(TOTAL_EVENTOS) { p = aplicar(p, acerto) }
        assertEquals(StatusJogo.VENCEU, p.status)
        assertEquals(RECOMPENSA_FINAL, p.recompensa)
        assertEquals(5, p.vida)
    }
}
