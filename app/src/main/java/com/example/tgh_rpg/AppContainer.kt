package com.example.tgh_rpg

import android.content.Context
import com.example.tgh_rpg.data.local.AppDatabase
import com.example.tgh_rpg.data.local.SeedLoader
import com.example.tgh_rpg.data.repository.EncontroRepository

/* "Caixa de ferramentas" do app: cria cada peça uma vez e entrega para quem precisar.
  Em bons termos vai criar a bomba toda, cri o banco, os DAOs, o repository e o seed
  uma vez só
*/
class AppContainer(context: Context) {
    val db: AppDatabase = AppDatabase.getDatabase(context)
    val encontroDao = db.encontroDao()
    val personagemDao = db.personagemDao()
    val encontroRepository = EncontroRepository(encontroDao)
    val seedLoader = SeedLoader(context.applicationContext, encontroDao)
}
