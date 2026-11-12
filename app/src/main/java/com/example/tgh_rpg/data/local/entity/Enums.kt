package com.example.tgh_rpg.data.local.entity

//aqui ficam os valores fechados, salva um enum como texto com o nome da constante sem precisar de conversor.

enum class TipoEvento { COMBATE, ARMADILHA, TESOURO, ENIGMA, DESCANSO,
    ENCONTRO, BOSS
}

enum class StatusPersonagem {
    EM_ANDAMENTO, MORTO, PORTA_SELADA, VENCEU
}