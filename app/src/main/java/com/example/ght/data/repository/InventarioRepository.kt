package com.example.ght.data.repository

import com.example.ght.data.Item
import com.example.ght.data.agruparPorNome
import com.example.ght.data.local.ItemDao
import com.example.ght.data.paraEntity
import com.example.ght.data.paraItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Inventário persistido no Room. Pertence ao personagem do save atual: some junto
 * com ele quando ele morre ou quando um novo jogo é iniciado (ver [PersonagemRepository]).
 */
class InventarioRepository(
    private val itemDao: ItemDao
) {
    fun observarDoAtivo(): Flow<List<Item>> =
        itemDao.observarDoAtivo().map { lista -> agruparPorNome(lista.map { it.paraItem() }) }

    suspend fun adicionar(personagemId: Long, item: Item): Result<Unit> =
        try {
            itemDao.inserir(item.paraEntity(personagemId))
            Result.success(Unit)
        } catch (erro: Exception) {
            Result.failure(erro)
        }
}
