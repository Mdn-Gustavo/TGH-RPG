package com.example.tgh_rpg.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "eventos")
data class EventoEntity(
    @PrimaryKey
    val id: Int,
    val titulo: String,
    val tipo: TipoEvento,
    val texto: String,
    val escolhaJson: String,
    val atualizadaEm: Long = 0L
)


