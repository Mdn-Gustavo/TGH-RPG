package com.example.ght

import android.app.Application
import android.util.Log
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class CasteloApp : Application() {
    lateinit var container: AppContainer
        private set

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val syncListeners = mutableListOf<ListenerRegistration>()

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)

        // Carrega encontros locais iniciais caso o Room esteja vazio
        appScope.launch {
            container.seedLoader.carregarSeVazio()
        }

        // Inicia sincronização em tempo real entre Cloud Firestore e Room (com fallback offline)
        container.encontroRepository.iniciarSincronizacao(appScope) { erro ->
            Log.w("CasteloApp", "Sincronização de encontros indisponível (modo offline): ${erro.message}")
        }?.let { syncListeners.add(it) }

        container.personagemRepository.iniciarSincronizacao(appScope) { erro ->
            Log.w("CasteloApp", "Sincronização de personagens indisponível (modo offline): ${erro.message}")
        }?.let { syncListeners.add(it) }
    }
}
