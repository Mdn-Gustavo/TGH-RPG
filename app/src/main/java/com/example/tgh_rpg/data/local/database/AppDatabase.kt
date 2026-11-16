package com.example.tgh_rpg.data.local.database

import com.example.tgh_rpg.data.local.Converters
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.tgh_rpg.data.local.dao.EventoDao
import com.example.tgh_rpg.data.local.dao.PersonagemDao
import com.example.tgh_rpg.data.local.entity.EventoEntity
import com.example.tgh_rpg.data.local.entity.PersonagemEntity

// Declares the local Room tables and the DAO interfaces used to access them.
@Database(
    entities = [PersonagemEntity::class, EventoEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    // Room supplies the concrete DAO implementations at runtime.
    abstract fun personagemDao(): PersonagemDao
    abstract fun eventoDao(): EventoDao

    companion object {
        // Reuse a single database connection throughout the app process.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                // Use the application context so the database does not retain an Activity.
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}