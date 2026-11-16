package com.example.ght

import android.content.Context
import com.example.ght.data.local.AppDatabase
import com.example.ght.data.local.SeedLoader
import com.example.ght.data.repository.EncontroRepository
import com.example.ght.data.repository.PersonagemRepository

class AppContainer(context: Context) {
    val db: AppDatabase = AppDatabase.getDatabase(context)
    val encontroDao = db.encontroDao()
    val personagemDao = db.personagemDao()
    val encontroRepository = EncontroRepository(encontroDao)
    val personagemRepository = PersonagemRepository(personagemDao)
    val seedLoader = SeedLoader(context.applicationContext, encontroDao)
}
