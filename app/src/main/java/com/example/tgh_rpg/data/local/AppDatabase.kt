package com.example.tgh_rpg.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [EncontroEntity::class, PersonagemEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun encontroDao(): EncontroDao
    abstract fun personagemDao(): PersonagemDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Um banco só para o app inteiro (singleton) menos fuck
        fun getDatabase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java, "castelo.db"
                )
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
