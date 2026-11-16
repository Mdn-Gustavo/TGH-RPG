package com.example.tgh_rpg.data.local.entity

import kotlinx.serialization.Serializable

// These finite choices are stored by name; Room's converter handles their database representation.
@Serializable
enum class TipoEvento { COMBATE, ARMADILHA, TESOURO, ENIGMA, DESCANSO,
    ENCONTRO, BOSS
}

// Represents the current outcome or lifecycle state of a character.
enum class StatusPersonagem {
    EM_ANDAMENTO, MORTO, PORTA_SELADA, VENCEU
}