package com.example.tgh_rpg

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/* Esse arquivo é o de aplication, que vai rodar antes de qualquer tela e dispara o
 carregamento da seed */

class CasteloApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        // Na primeira abertura o banco está vazio: carrega os encontros do seed
        CoroutineScope(Dispatchers.IO).launch { container.seedLoader.carregarSeVazio() }
    }
}
