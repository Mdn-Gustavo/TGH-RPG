package com.example.ght.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class Item(
    val nome: String,
    val efeito: String,
    val bonusForca: Int = 0,
    val bonusAgilidade: Int = 0,
    val bonusMente: Int = 0,
    val curaVida: Int = 0
)

object Inventario {
    private val _itens = MutableStateFlow<List<Item>>(emptyList())
    val itens = _itens.asStateFlow()

    fun adicionar(item: Item) {
        _itens.update { lista ->
            val posicao = lista.indexOfLast { it.nome == item.nome }
            if (posicao == -1) {
                lista + item
            } else {
                lista.toMutableList().apply { add(posicao + 1, item) }
            }
        }
    }

    fun limpar() {
        _itens.value = emptyList()
    }
}
