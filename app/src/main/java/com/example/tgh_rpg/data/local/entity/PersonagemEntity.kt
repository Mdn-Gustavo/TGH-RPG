package com.example.tgh_rpg.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "personagem")
data class PersonagemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val hpMaximo: Int = 5,
    val hpAtual: Int = 5,
    val forca: Int = 5,
    val agilidade: Int = 7,
    val mente: Int = 7,
    val salaAtual: Int = 1, //se valor salaAtual atingir int 13, vai pro boss automaticamente
    val eventosVistos: List<String> = emptyList(), // evita repetir eventos ja vistos
    val marcas: List<String> = emptyList(), //guarda marcas da historia, ex: ignorou um artefato
    val status: StatusPersonagem = StatusPersonagem.EM_ANDAMENTO,
    val criadoEm: Long = System.currentTimeMillis()
)
