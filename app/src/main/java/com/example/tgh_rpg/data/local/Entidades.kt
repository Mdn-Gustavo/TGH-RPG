package com.example.tgh_rpg.data.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Atributo { FORCA, AGILIDADE, MENTE }

// Uma opção do encontro. NÃO é @Entity: não vira tabela própria. Como estamos pegando algo
// mais simples não pensei em estabelecer tabelas
// Todos os campos têm valor padrão porque o Firestore exige isso (ele cria o objeto vazio
// e depois preenche).
data class Opcao(
    val rotulo: String = "",
    val atributo: Atributo = Atributo.FORCA,   // qual atributo esta opção testa
    val dificuldade: Int = 10,                 // d20 + atributo tem que dar pelo menos isso
    val textoSucesso: String = "",
    val textoFalha: String = ""
)

@Entity(tableName = "encontros")
data class EncontroEntity(
    @PrimaryKey val id: String = "",
    val titulo: String = "",
    val texto: String = "",
    // prefixos das opcoes das 3 opcoes de cada um dos eventos (pensei em colocar 3 escolhas
    // para cada evento sem diretamente serem escolhas que importam para o resto da historia)
    @Embedded(prefix = "a_") val opcaoA: Opcao = Opcao(),
    @Embedded(prefix = "b_") val opcaoB: Opcao = Opcao(),
    @Embedded(prefix = "c_") val opcaoC: Opcao = Opcao(),
    val atualizadoEm: Long = 0L
)

enum class StatusJogo { EM_ANDAMENTO, MORTO, VENCEU }

@Entity(tableName = "personagens")
data class PersonagemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nome: String,
    val forca: Int,
    val agilidade: Int,
    val mente: Int,
    val vida: Int = 5,
    val vidaMax: Int = 5,
    val eventos: Int = 0,            // quantos encontros já jogou
    val acertos: Int = 0,            // quantos testes passou (usado no ranking), seria legal termos um ranking de pontuacao dos jogadores que ja participaram do game
    val status: StatusJogo = StatusJogo.EM_ANDAMENTO,
    val recompensa: String = "",     // vazio até vencer. uma ideia abstrata, talvez retire depois, n afeta nada no jogo.
    val criadoEm: Long = System.currentTimeMillis()
)
