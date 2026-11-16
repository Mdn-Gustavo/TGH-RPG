package com.example.tgh_rpg.data.local.repository

import com.example.tgh_rpg.data.local.dao.EventoDao
import com.example.tgh_rpg.data.local.entity.EventoEntity
import com.example.tgh_rpg.data.remote.toEventoEntity
import com.example.tgh_rpg.data.remote.toFirestoreMap
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

// Coordinates event persistence between Room, which feeds the UI, and Firestore, which stores the cloud copy.
class EventoRepository(
    private val eventoDao: EventoDao,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    // Keep all remote events together in Firestore's "eventos" collection.
    private val collection = firestore.collection("eventos")

    // The UI observes Room so it can react to changes and read cached events while offline.
    fun observarEventos(): Flow<List<EventoEntity>> = eventoDao.observarEventos()

    // Listen for cloud changes and mirror them into Room; return the handle so the caller can stop listening.
    fun iniciarSincronizacao(
        scope: CoroutineScope,
        aoFalhar: (Exception) -> Unit
    ): ListenerRegistration = collection.addSnapshotListener { snapshot, error ->
        if (error != null) {
            // Report sync failures instead of silently ignoring them.
            aoFalhar(error)
            return@addSnapshotListener
        }
        if (snapshot == null) return@addSnapshotListener

        // Firestore callbacks are not suspend functions, so use the provided scope for Room operations.
        scope.launch {
            try {
                snapshot.documentChanges.forEach { change ->
                    val id = change.document.id.toIntOrNull()
                        ?: error("ID de evento inválido no Firestore: ${change.document.id}")
                    // Apply remote deletes locally; insert or replace rows for added and changed documents.
                    if (change.type == DocumentChange.Type.REMOVED) {
                        eventoDao.excluirPorId(id)
                    } else {
                        eventoDao.salvarSincronizado(change.document.toEventoEntity())
                    }
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                aoFalhar(exception)
            }
        }
    }

    // Save locally first for an immediate Room Flow update, then copy the event to Firestore.
    suspend fun inserir(evento: EventoEntity) {
        eventoDao.inserir(evento)
        salvarNoFirestore(evento)
    }

    // Update the local row and its corresponding cloud document.
    suspend fun atualizar(evento: EventoEntity) {
        eventoDao.atualizar(evento)
        salvarNoFirestore(evento)
    }

    // Delete from both stores so the listener keeps the local cache in sync with Firestore.
    suspend fun excluir(evento: EventoEntity) {
        eventoDao.excluir(evento)
        withContext(Dispatchers.IO) {
            Tasks.await(collection.document(evento.id.toString()).delete())
        }
    }

    // Convert the entity to Firestore fields and await completion on IO to avoid blocking the UI thread.
    private suspend fun salvarNoFirestore(evento: EventoEntity) {
        withContext(Dispatchers.IO) {
            Tasks.await(
                collection.document(evento.id.toString()).set(evento.toFirestoreMap())
            )
        }
    }
}