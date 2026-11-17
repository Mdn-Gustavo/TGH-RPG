package com.example.ght.data.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

enum class Atributo { FORCA, AGILIDADE, MENTE }

data class Opcao(
    val rotulo: String = "",
    val atributo: Atributo = Atributo.FORCA,
    val dificuldade: Int = 10,
    val textoSucesso: String = "",
    val textoFalha: String = "",
    val chanceBatalha: Int? = null,
    val inimigoNome: String? = null,
    val inimigoVida: Int = 15,
    val inimigoDanoMin: Int = 2,
    val inimigoDanoMax: Int = 5,
    val itemNome: String? = null,
    val itemEfeito: String? = null,
    val itemBonusForca: Int = 0,
    val itemBonusAgilidade: Int = 0,
    val itemBonusMente: Int = 0,
    val itemCuraVida: Int = 0
)

@Entity(tableName = "encontros")
data class EncontroEntity(
    @PrimaryKey val id: String = "",
    val titulo: String = "",
    val texto: String = "",
    @Embedded(prefix = "a_") val opcaoA: Opcao = Opcao(),
    @Embedded(prefix = "b_") val opcaoB: Opcao = Opcao(),
    @Embedded(prefix = "c_") val opcaoC: Opcao = Opcao(),
    val atualizadoEm: Long = 0L
)

const val PREFIXO_ID_ADM = "adm_"

val EncontroEntity.criadoPeloAdm: Boolean
    get() = id.startsWith(PREFIXO_ID_ADM)

val EncontroEntity.dificuldadeMedia: Double
    get() = (opcaoA.dificuldade + opcaoB.dificuldade) / 2.0

enum class StatusJogo { EM_ANDAMENTO, MORTO, VENCEU }

@Entity(tableName = "personagens")
data class PersonagemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nome: String = "",
    val classe: String = "A",
    val forca: Int = 3,
    val agilidade: Int = 3,
    val mente: Int = 3,
    val vida: Int = 20,
    val vidaMax: Int = 20,
    val eventos: Int = 0,
    val acertos: Int = 0,
    val status: StatusJogo = StatusJogo.EM_ANDAMENTO,
    val recompensa: String = "",
    val criadoEm: Long = System.currentTimeMillis()
)

@Entity(tableName = "itens", indices = [Index("personagemId")])
data class ItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val personagemId: Long,
    val nome: String,
    val efeito: String,
    val bonusForca: Int = 0,
    val bonusAgilidade: Int = 0,
    val bonusMente: Int = 0,
    val curaVida: Int = 0
)
