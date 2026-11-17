package com.example.ght.data.repository

import com.example.ght.data.local.ItemDao
import com.example.ght.data.local.PersonagemDao
import com.example.ght.data.local.PersonagemEntity
import com.example.ght.data.local.StatusJogo
import com.example.ght.data.remote.toFirestoreMap
import com.example.ght.data.remote.toPersonagemEntity
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PersonagemRepository(
    private val personagemDao: PersonagemDao,
    private val itemDao: ItemDao,
    private val firestore: FirebaseFirestore? = runCatching { FirebaseFirestore.getInstance() }.getOrNull()
) {
    private val collection = firestore?.collection("personagens")

    fun observarPersonagens(): Flow<List<PersonagemEntity>> =
        personagemDao.observarTodos()

    fun observarAtivo(): Flow<PersonagemEntity?> =
        personagemDao.observarAtivo()

    suspend fun buscarAtivo(): Result<PersonagemEntity?> =
        try {
            Result.success(personagemDao.buscarAtivo())
        } catch (erro: Exception) {
            Result.failure(erro)
        }

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
            if (snapshot == null) return@addSnapshotListener

            scope.launch {
                try {
                    snapshot.documentChanges.forEach { change ->
                        val id = change.document.id.toLongOrNull()
                            ?: error("ID de personagem inválido no Firestore: ${change.document.id}")
                        if (change.type == DocumentChange.Type.REMOVED) {
                            personagemDao.excluirPorId(id)
                        } else {
                            personagemDao.salvarSincronizado(change.document.toPersonagemEntity())
                        }
                    }
                    itemDao.apagarOrfaos()
                } catch (exception: CancellationException) {
                    throw exception
                } catch (exception: Exception) {
                    aoFalhar(exception)
                }
            }
        }
    }

    suspend fun inserir(personagem: PersonagemEntity): Result<Long> =
        try {
            val id = personagemDao.inserir(personagem)
            salvarNoFirestore(personagem.copy(id = id))
            Result.success(id)
        } catch (erro: Exception) {
            Result.failure(erro)
        }

    suspend fun atualizar(personagem: PersonagemEntity): Result<Unit> =
        try {
            personagemDao.atualizar(personagem)
            salvarNoFirestore(personagem)
            Result.success(Unit)
        } catch (erro: Exception) {
            Result.failure(erro)
        }

    suspend fun encerrarAtivos(): Result<Unit> =
        try {
            val ativo = personagemDao.buscarAtivo()
            personagemDao.encerrarAtivos()
            itemDao.apagarOrfaos()
            if (ativo != null) {
                salvarNoFirestore(ativo.copy(status = StatusJogo.MORTO))
            }
            Result.success(Unit)
        } catch (erro: Exception) {
            Result.failure(erro)
        }

    suspend fun excluir(personagem: PersonagemEntity): Result<Unit> =
        try {
            personagemDao.apagar(personagem)
            itemDao.apagarOrfaos()
            collection?.document(personagem.id.toString())?.delete()
            Result.success(Unit)
        } catch (erro: Exception) {
            Result.failure(erro)
        }

    private fun salvarNoFirestore(personagem: PersonagemEntity) {
        collection?.document(personagem.id.toString())?.set(personagem.toFirestoreMap())
    }
}
