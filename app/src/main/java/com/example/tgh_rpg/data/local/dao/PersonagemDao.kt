package com.example.tgh_rpg.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.tgh_rpg.data.local.entity.PersonagemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonagemDao {
    // Keep the character list ordered by creation time and expose database changes reactively.
    @Query("SELECT * FROM personagem ORDER BY criadoEm DESC")
    fun observarPersonagens(): Flow<List<PersonagemEntity>>

    // Insert a new character and return its generated Room ID.
    @Insert
    suspend fun inserir(personagem: PersonagemEntity): Long

    // Copy a character received from Firestore into Room, replacing an existing row with the same ID.
    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun salvarSincronizado(personagem: PersonagemEntity)

    // Update the Room row identified by the character's primary key.
    @Update
    suspend fun atualizar(personagem: PersonagemEntity)

    // Remove the matching character from Room.
    @Delete
    suspend fun excluir(personagem: PersonagemEntity)

    // Remove a cached character when its Firestore document is deleted.
    @Query("DELETE FROM personagem WHERE id = :id")
    suspend fun excluirPorId(id: Int)
}