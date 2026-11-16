package com.example.tgh_rpg.data.local.repository

import com.example.tgh_rpg.data.local.dao.PersonagemDao
import com.example.tgh_rpg.data.local.entity.PersonagemEntity
import com.example.tgh_rpg.data.remote.toFirestoreMap
import com.example.tgh_rpg.data.remote.toPersonagemEntity
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

// Keeps Room as the reactive local cache while mirroring character writes to Firestore.
class PersonagemRepository(
    private val personagemDao: PersonagemDao,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    // Each character is stored as a document in this Firestore collection.
    private val collection = firestore.collection("personagens")

    // The UI collects this Room Flow, so local and synchronized changes update the screen.
    fun observarPersonagens(): Flow<List<PersonagemEntity>> =
        personagemDao.observarPersonagens()

    // Mirror Firestore additions, edits, and deletions into Room and return a listener handle for cleanup.
    fun iniciarSincronizacao(
        scope: CoroutineScope,
        aoFalhar: (Exception) -> Unit
    ): ListenerRegistration = collection.addSnapshotListener { snapshot, error ->
        if (error != null) {
            // Send listener errors to the ViewModel so they can be shown to the user.
            aoFalhar(error)
            return@addSnapshotListener
        }
        if (snapshot == null) return@addSnapshotListener

        // Launch a coroutine because Room DAO operations are suspend functions.
        scope.launch {
            try {
                snapshot.documentChanges.forEach { change ->
                    val id = change.document.id.toIntOrNull()
                        ?: error("ID de personagem inválido no Firestore: ${change.document.id}")
                    // A remote deletion removes the local cache row; other changes upsert its latest data.
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

    // Insert locally first to get Room's generated ID, then use that ID for the Firestore document.
    suspend fun inserir(personagem: PersonagemEntity) {
        val id = personagemDao.inserir(personagem).toInt()
        salvarNoFirestore(personagem.copy(id = id))
    }

    // Update the Room cache and overwrite the matching Firestore document.
    suspend fun atualizar(personagem: PersonagemEntity) {
        personagemDao.atualizar(personagem)
        salvarNoFirestore(personagem)
    }

    // Delete the character from both the local table and its remote document.
    suspend fun excluir(personagem: PersonagemEntity) {
        personagemDao.excluir(personagem)
        withContext(Dispatchers.IO) {
            Tasks.await(collection.document(personagem.id.toString()).delete())
        }
    }

    // Convert the entity to Firestore fields and await the write on an IO thread.
    private suspend fun salvarNoFirestore(personagem: PersonagemEntity) {
        withContext(Dispatchers.IO) {
            Tasks.await(
                collection.document(personagem.id.toString()).set(personagem.toFirestoreMap())
            )
        }
    }
}