package com.example.ght.data.repository

import com.example.ght.data.local.EncontroDao
import com.example.ght.data.local.EncontroEntity
import kotlinx.coroutines.flow.Flow

class EncontroRepository(private val dao: EncontroDao) {

    val encontros: Flow<List<EncontroEntity>> = dao.observarTodos()

    suspend fun sortearProximo(excetoId: String): Result<EncontroEntity?> =
        try {
            Result.success(dao.sortearUm(excetoId))
        } catch (erro: Exception) {
            Result.failure(erro)
        }

    suspend fun salvar(encontro: EncontroEntity): Result<Unit> =
        try {
            dao.salvar(encontro.copy(atualizadoEm = System.currentTimeMillis()))
            Result.success(Unit)
        } catch (erro: Exception) {
            Result.failure(erro)
        }

    suspend fun apagar(encontro: EncontroEntity): Result<Unit> =
        try {
            dao.apagar(encontro)
            Result.success(Unit)
        } catch (erro: Exception) {
            Result.failure(erro)
        }
}
