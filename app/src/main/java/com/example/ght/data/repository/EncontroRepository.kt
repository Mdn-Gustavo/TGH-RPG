package com.example.ght.data.repository

import com.example.ght.data.local.EncontroDao
import com.example.ght.data.local.EncontroEntity
import com.example.ght.data.local.criadoPeloAdm
import com.example.ght.data.remote.toEncontroEntity
import com.example.ght.data.remote.toFirestoreMap
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class EncontroRepository(
    private val dao: EncontroDao,
    private val firestore: FirebaseFirestore? = runCatching { FirebaseFirestore.getInstance() }.getOrNull()
) {
    private val collection = firestore?.collection("encontros")

    val encontros: Flow<List<EncontroEntity>> = dao.observarTodos()

    val encontrosAdm: Flow<List<EncontroEntity>> =
        dao.observarTodos().map { lista -> lista.filter { it.criadoPeloAdm } }

    suspend fun buscarDoAdm(id: String): EncontroEntity? =
        dao.buscarPorId(id)?.takeIf { it.criadoPeloAdm }

    fun iniciarSincronizacao(
        scope: CoroutineScope,
        aoFalhar: (Exception) -> Unit
    ): ListenerRegistration? {
        val col = collection ?: return null
        return col.addSnapshotListener { snapshot, error ->
            if (error != null) {
                aoFalhar(error)
                return@addSnapshotListener
            }
            if (snapshot == null || snapshot.isEmpty) return@addSnapshotListener

            scope.launch {
                try {
                    snapshot.documentChanges.forEach { change ->
                        if (change.type == DocumentChange.Type.REMOVED) {
                            dao.apagarPorId(change.document.id)
                        } else {
                            dao.salvar(change.document.toEncontroEntity())
                        }
                    }
                } catch (exception: CancellationException) {
                    throw exception
                } catch (exception: Exception) {
                    aoFalhar(exception)
                }
            }
        }
    }

    suspend fun buscarPorId(id: String): Result<EncontroEntity?> =
        try {
            Result.success(dao.buscarPorId(id))
        } catch (erro: Exception) {
            Result.failure(erro)
        }

    suspend fun sortearProximo(excetoId: String): Result<EncontroEntity?> =
        try {
            Result.success(dao.sortearUm(excetoId))
        } catch (erro: Exception) {
            Result.failure(erro)
        }

    suspend fun salvar(encontro: EncontroEntity): Result<Unit> {
        if (!encontro.criadoPeloAdm) return Result.failure(erroEncontroNativo())
        return try {
            val atualizado = encontro.copy(atualizadoEm = System.currentTimeMillis())
            dao.salvar(atualizado)
            espelharNoFirestore(atualizado)
            Result.success(Unit)
        } catch (erro: Exception) {
            Result.failure(erro)
        }
    }

    suspend fun apagar(encontro: EncontroEntity): Result<Unit> {
        if (!encontro.criadoPeloAdm) return Result.failure(erroEncontroNativo())
        return try {
            dao.apagar(encontro)
            removerDoFirestore(encontro.id)
            Result.success(Unit)
        } catch (erro: Exception) {
            Result.failure(erro)
        }
    }

    private fun erroEncontroNativo() =
        IllegalArgumentException("Encontros nativos do jogo não podem ser alterados")

    private fun espelharNoFirestore(encontro: EncontroEntity) {
        collection?.document(encontro.id)?.set(encontro.toFirestoreMap())
    }

    private fun removerDoFirestore(id: String) {
        collection?.document(id)?.delete()
    }
}
