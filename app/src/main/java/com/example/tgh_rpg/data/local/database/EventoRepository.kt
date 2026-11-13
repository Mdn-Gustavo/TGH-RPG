package com.example.tgh_rpg.data.local.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.example.tgh_rpg.data.local.dao.EventoDao
import kotlinx.coroutines.tasks.await

class EventoRepository(private val eventoDao: EventoDao) {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val eventoDao: EventoDao = eventoDao
} {
    suspend fun getEventos() {
        try {
            val snapshot = firestore.collection("eventos").get().await()
            val eventos = snapshot.documents.mapNotNull { it.toObject(EventoEntity::class.java) }
            eventoDao.insertAll(eventos)
        } catch (e: Exception) {
            val eventos = eventoDao.getAllEventos()
        }
    }
}