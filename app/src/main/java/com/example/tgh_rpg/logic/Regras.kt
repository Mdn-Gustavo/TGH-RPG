package com.example.tgh_rpg.logic

import com.example.tgh_rpg.data.local.Atributo
import com.example.tgh_rpg.data.local.Opcao
import com.example.tgh_rpg.data.local.PersonagemEntity
import com.example.tgh_rpg.data.local.StatusJogo
import kotlin.random.Random

/*
* O arquivo de regras esta separado pois não depende nem de banco nem de tela, assim podemos
* testar rapidamente, facilmente mudavel e qualquer um vai pode usar
* */
const val TOTAL_EVENTOS = 10
const val RECOMPENSA_FINAL = "Tesouro do Castelo"

// Fichas iniciais: 9 pontos em força/agilidade/mente
enum class Classe(val forca: Int, val agilidade: Int, val mente: Int) {
    /* Pedi pra IA separar essa parte mas quero estabelecer melhor posteriormente como fica
     o quesito de nivel inicial e qual o nivel maximo de pontos que o persongem pode atingir
      no total para poder ir direto para o encontro com o Boss final */

    GUERREIRO(5, 3, 1), LADINO(1, 5, 3), MAGO(1, 3, 5)
}

fun novoPersonagem(nome: String, classe: Classe) =
    PersonagemEntity(nome = nome, forca = classe.forca, agilidade = classe.agilidade, mente = classe.mente)

data class Resultado(
    val sucesso: Boolean, val texto: String,
    val rolagem: Int, val bonus: Int, val dificuldade: Int
)

fun valorDoAtributo(p: PersonagemEntity, a: Atributo): Int = when (a) {
    Atributo.FORCA -> p.forca
    Atributo.AGILIDADE -> p.agilidade
    Atributo.MENTE -> p.mente
}

// d20 + atributo >= dificuldade. 1 natural sempre erra, 20 natural sempre acerta.
fun resolver(p: PersonagemEntity, opcao: Opcao, rolagem: Int = Random.nextInt(1, 21)): Resultado {
    val bonus = valorDoAtributo(p, opcao.atributo)
    val sucesso = rolagem == 20 || (rolagem != 1 && rolagem + bonus >= opcao.dificuldade)
    val texto = if (sucesso) opcao.textoSucesso else opcao.textoFalha
    return Resultado(sucesso, texto, rolagem, bonus, opcao.dificuldade)
}

fun aplicar(p: PersonagemEntity, r: Resultado): PersonagemEntity {
    val vida = if (r.sucesso) p.vida else p.vida - 1
    val eventos = p.eventos + 1
    val acertos = if (r.sucesso) p.acertos + 1 else p.acertos
    val status = when {
        vida <= 0 -> StatusJogo.MORTO
        eventos >= TOTAL_EVENTOS -> StatusJogo.VENCEU
        else -> StatusJogo.EM_ANDAMENTO
    }
    return p.copy(
        vida = vida, eventos = eventos, acertos = acertos, status = status,
        recompensa = if (status == StatusJogo.VENCEU) RECOMPENSA_FINAL else ""
    )
}
