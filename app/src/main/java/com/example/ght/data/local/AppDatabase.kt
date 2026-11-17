package com.example.ght.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [EncontroEntity::class, PersonagemEntity::class, ItemEntity::class],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun encontroDao(): EncontroDao
    abstract fun personagemDao(): PersonagemDao
    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `itens` (" +
                        "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "`personagemId` INTEGER NOT NULL, " +
                        "`nome` TEXT NOT NULL, " +
                        "`efeito` TEXT NOT NULL, " +
                        "`bonusForca` INTEGER NOT NULL, " +
                        "`bonusAgilidade` INTEGER NOT NULL, " +
                        "`bonusMente` INTEGER NOT NULL, " +
                        "`curaVida` INTEGER NOT NULL)"
                )
                db.execSQL(
                    "CREATE INDEX IF NOT EXISTS `index_itens_personagemId` " +
                        "ON `itens` (`personagemId`)"
                )
            }
        }

        fun getDatabase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java, "castelo.db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
