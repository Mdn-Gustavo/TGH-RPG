package com.example.ght.data.repository

import com.example.ght.data.local.PersonagemDao
import com.example.ght.data.local.PersonagemEntity
import com.example.ght.data.remote.toFirestoreMap
import com.example.ght.data.remote.toPersonagemEntity
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Mantém Room como cache local reativo e espelha alterações de personagem no Firestore (trazido do TGH-att)
class PersonagemRepository(
    private val personagemDao: PersonagemDao,
    private val firestore: FirebaseFirestore? = runCatching { FirebaseFirestore.getInstance() }.getOrNull()
) {
    private val collection = firestore?.collection("personagens")

    fun observarPersonagens(): Flow<List<PersonagemEntity>> =
        personagemDao.observarTodos()

    fun observarAtivo(): Flow<PersonagemEntity?> =
        personagemDao.observarAtivo()

    suspend fun buscarAtivo(): PersonagemEntity? =
        personagemDao.buscarAtivo()

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
                } catch (exception: CancellationException) {
                    throw exception
                } catch (exception: Exception) {
                    aoFalhar(exception)
                }
            }
        }
    }

    suspend fun inserir(personagem: PersonagemEntity): Long {
        val id = personagemDao.inserir(personagem)
        salvarNoFirestore(personagem.copy(id = id))
        return id
    }

    suspend fun atualizar(personagem: PersonagemEntity) {
        personagemDao.atualizar(personagem)
        salvarNoFirestore(personagem)
    }

    suspend fun encerrarAtivos() {
        val ativo = personagemDao.buscarAtivo()
        personagemDao.encerrarAtivos()
        if (ativo != null) {
            salvarNoFirestore(ativo.copy(status = com.example.ght.data.local.StatusJogo.MORTO))
        }
    }

    suspend fun excluir(personagem: PersonagemEntity) {
        personagemDao.apagar(personagem)
        collection?.document(personagem.id.toString())?.delete()
    }

    private fun salvarNoFirestore(personagem: PersonagemEntity) {
        collection?.document(personagem.id.toString())?.set(personagem.toFirestoreMap())
    }
}
