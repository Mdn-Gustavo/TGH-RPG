package com.example.ght.data

import com.example.ght.data.local.ItemEntity

data class Item(
    val nome: String,
    val efeito: String,
    val bonusForca: Int = 0,
    val bonusAgilidade: Int = 0,
    val bonusMente: Int = 0,
    val curaVida: Int = 0
)

fun Item.paraEntity(personagemId: Long) = ItemEntity(
    personagemId = personagemId,
    nome = nome,
    efeito = efeito,
    bonusForca = bonusForca,
    bonusAgilidade = bonusAgilidade,
    bonusMente = bonusMente,
    curaVida = curaVida
)

fun ItemEntity.paraItem() = Item(
    nome = nome,
    efeito = efeito,
    bonusForca = bonusForca,
    bonusAgilidade = bonusAgilidade,
    bonusMente = bonusMente,
    curaVida = curaVida
)

fun agruparPorNome(itens: List<Item>): List<Item> =
    itens.fold(emptyList<Item>()) { lista, item ->
        val posicao = lista.indexOfLast { it.nome == item.nome }
        if (posicao == -1) {
            lista + item
        } else {
            lista.toMutableList().apply { add(posicao + 1, item) }
        }
    }
