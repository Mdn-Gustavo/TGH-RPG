package com.example.tgh_rpg.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.tgh_rpg.data.local.entity.EventoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventoDao {
    // Room reruns this query when the table changes and emits the newest events through Flow.
    @Query("SELECT * FROM eventos ORDER BY atualizadaEm DESC")
    fun observarEventos(): Flow<List<EventoEntity>>

    // Add a new local event and return Room's generated row ID.
    @Insert
    suspend fun inserir(evento: EventoEntity): Long

    // Insert cloud events into Room, replacing a row if its primary key already exists.
    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun salvarSincronizado(evento: EventoEntity)

    // Update an existing Room row using the entity's primary key.
    @Update
    suspend fun atualizar(evento: EventoEntity)

    // Remove the matching Room row.
    @Delete
    suspend fun excluir(evento: EventoEntity)

    // Remove a cached event when its Firestore document is deleted.
    @Query("DELETE FROM eventos WHERE id = :id")
    suspend fun excluirPorId(id: Int)
}
