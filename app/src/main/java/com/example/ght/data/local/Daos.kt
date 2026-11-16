package com.example.ght.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
abstract class EncontroDao {

    @Query("SELECT * FROM encontros ORDER BY titulo")
    abstract fun observarTodos(): Flow<List<EncontroEntity>>

    @Query("SELECT * FROM encontros WHERE id != :excetoId ORDER BY RANDOM() LIMIT 1")
    abstract suspend fun sortearUm(excetoId: String): EncontroEntity?

    @Query("SELECT COUNT(*) FROM encontros")
    abstract suspend fun contar(): Int

    @Upsert abstract suspend fun salvar(encontro: EncontroEntity)
    @Upsert abstract suspend fun salvarTodos(encontros: List<EncontroEntity>)
    @Delete abstract suspend fun apagar(encontro: EncontroEntity)

    @Query("DELETE FROM encontros WHERE id = :id")
    abstract suspend fun apagarPorId(id: String)

    @Query("DELETE FROM encontros")
    abstract suspend fun limparTudo()

    @Transaction
    open suspend fun substituirTodos(novos: List<EncontroEntity>) {
        if (novos.isEmpty()) return
        limparTudo()
        salvarTodos(novos)
    }
}

@Dao
interface PersonagemDao {

    @Query("SELECT * FROM personagens ORDER BY criadoEm DESC")
    fun observarTodos(): Flow<List<PersonagemEntity>>

    @Query("SELECT * FROM personagens WHERE status = 'EM_ANDAMENTO' ORDER BY criadoEm DESC LIMIT 1")
    fun observarAtivo(): Flow<PersonagemEntity?>

    @Query("SELECT * FROM personagens WHERE status = 'EM_ANDAMENTO' ORDER BY criadoEm DESC LIMIT 1")
    suspend fun buscarAtivo(): PersonagemEntity?

    @Query("SELECT * FROM personagens WHERE id = :id")
    suspend fun buscar(id: Long): PersonagemEntity?

    @Insert
    suspend fun inserir(personagem: PersonagemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salvarSincronizado(personagem: PersonagemEntity)

    @Update
    suspend fun atualizar(personagem: PersonagemEntity)

    @Delete
    suspend fun apagar(personagem: PersonagemEntity)

    @Query("DELETE FROM personagens WHERE id = :id")
    suspend fun excluirPorId(id: Long)

    @Query("UPDATE personagens SET status = 'MORTO' WHERE status = 'EM_ANDAMENTO'")
    suspend fun encerrarAtivos()
}
