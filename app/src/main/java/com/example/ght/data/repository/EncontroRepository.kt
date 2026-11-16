package com.example.ght.data.repository

import com.example.ght.data.local.EncontroDao
import com.example.ght.data.local.EncontroEntity
import com.example.ght.data.remote.toEncontroEntity
import com.example.ght.data.remote.toFirestoreMap
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

// Repository de encontros: Room é a fonte local rápida e Cloud Firestore espelha as alterações
class EncontroRepository(
    private val dao: EncontroDao,
    private val firestore: FirebaseFirestore? = runCatching { FirebaseFirestore.getInstance() }.getOrNull()
) {
    private val collection = firestore?.collection("encontros")

    val encontros: Flow<List<EncontroEntity>> = dao.observarTodos()

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

    suspend fun sortearProximo(excetoId: String): Result<EncontroEntity?> =
        try {
            Result.success(dao.sortearUm(excetoId))
        } catch (erro: Exception) {
            Result.failure(erro)
        }

    suspend fun salvar(encontro: EncontroEntity): Result<Unit> =
        try {
            val atualizado = encontro.copy(atualizadoEm = System.currentTimeMillis())
            dao.salvar(atualizado)
            espelharNoFirestore(atualizado)
            Result.success(Unit)
        } catch (erro: Exception) {
            Result.failure(erro)
        }

    suspend fun apagar(encontro: EncontroEntity): Result<Unit> =
        try {
            dao.apagar(encontro)
            removerDoFirestore(encontro.id)
            Result.success(Unit)
        } catch (erro: Exception) {
            Result.failure(erro)
        }

    private fun espelharNoFirestore(encontro: EncontroEntity) {
        collection?.document(encontro.id)?.set(encontro.toFirestoreMap())
    }

    private fun removerDoFirestore(id: String) {
        collection?.document(id)?.delete()
    }
}
