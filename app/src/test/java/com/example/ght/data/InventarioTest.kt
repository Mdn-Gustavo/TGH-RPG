package com.example.ght.data

import org.junit.Assert.assertEquals
import org.junit.Test

class InventarioTest {

    private fun item(nome: String) = Item(nome = nome, efeito = "")

    @Test
    fun mantemAOrdemDeColetaQuandoNaoHaRepetidos() {
        val lista = agruparPorNome(listOf(item("Espada"), item("Poção"), item("Escudo")))
        assertEquals(listOf("Espada", "Poção", "Escudo"), lista.map { it.nome })
    }

    @Test
    fun itemRepetidoEntraLogoDepoisDoUltimoDeMesmoNome() {
        val lista = agruparPorNome(
            listOf(item("Poção"), item("Espada"), item("Poção"), item("Escudo"), item("Espada"))
        )
        assertEquals(listOf("Poção", "Poção", "Espada", "Espada", "Escudo"), lista.map { it.nome })
    }

    @Test
    fun listaVaziaContinuaVazia() {
        assertEquals(emptyList<Item>(), agruparPorNome(emptyList()))
    }
}
