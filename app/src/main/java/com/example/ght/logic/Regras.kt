package com.example.ght.logic

import com.example.ght.data.Item
import com.example.ght.data.local.Atributo
import com.example.ght.data.local.Opcao
import com.example.ght.data.local.PersonagemEntity
import com.example.ght.data.local.StatusJogo
import kotlin.random.Random

const val TOTAL_EVENTOS = 10
const val RECOMPENSA_FINAL = "Tesouro do Castelo"

enum class Classe(val letra: String, val forca: Int, val agilidade: Int, val mente: Int) {
    GUERREIRO("A", 5, 3, 1),
    LADINO("B", 1, 5, 3),
    MAGO("C", 1, 3, 5),
    CLERIGO("D", 3, 2, 4);

    companion object {
        fun daLetra(letra: String): Classe =
            entries.find { it.letra == letra } ?: GUERREIRO
    }
}

fun novoPersonagem(nome: String, classe: Classe) = PersonagemEntity(
    nome = nome,
    classe = classe.letra,
    forca = classe.forca,
    agilidade = classe.agilidade,
    mente = classe.mente
)

data class Resultado(
    val sucesso: Boolean,
    val texto: String,
    val rolagem: Int,
    val bonus: Int,
    val dificuldade: Int
)

fun valorDoAtributo(p: PersonagemEntity, a: Atributo): Int = when (a) {
    Atributo.FORCA -> p.forca
    Atributo.AGILIDADE -> p.agilidade
    Atributo.MENTE -> p.mente
}

fun resolver(p: PersonagemEntity, opcao: Opcao, rolagem: Int = Random.nextInt(1, 21)): Resultado {
    val bonus = valorDoAtributo(p, opcao.atributo)
    val sucesso = rolagem == 20 || (rolagem != 1 && rolagem + bonus >= opcao.dificuldade)
    val texto = if (sucesso) opcao.textoSucesso else opcao.textoFalha
    return Resultado(sucesso, texto, rolagem, bonus, opcao.dificuldade)
}

fun aplicar(p: PersonagemEntity, r: Resultado): PersonagemEntity {
    val vida = if (r.sucesso) p.vida else (p.vida - 2).coerceAtLeast(0)
    val eventos = p.eventos + 1
    val acertos = if (r.sucesso) p.acertos + 1 else p.acertos
    val status = when {
        vida <= 0 -> StatusJogo.MORTO
        eventos >= TOTAL_EVENTOS -> StatusJogo.VENCEU
        else -> StatusJogo.EM_ANDAMENTO
    }
    return p.copy(
        vida = vida,
        eventos = eventos,
        acertos = acertos,
        status = status,
        recompensa = if (status == StatusJogo.VENCEU) RECOMPENSA_FINAL else ""
    )
}

fun aplicarItem(p: PersonagemEntity, item: Item): PersonagemEntity {
    val novaVida = (p.vida + item.curaVida).coerceAtMost(p.vidaMax)
    return p.copy(
        forca = p.forca + item.bonusForca,
        agilidade = p.agilidade + item.bonusAgilidade,
        mente = p.mente + item.bonusMente,
        vida = novaVida
    )
}
