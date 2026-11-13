package com.example.tgh_rpg.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
abstract class EncontroDao {

    // a tela de admin se atualiza sozinha quando a tabela muda
    @Query("SELECT * FROM encontros ORDER BY titulo")
    abstract fun observarTodos(): Flow<List<EncontroEntity>>

    // nunca repete o encontro que acabou de sair, tudo sorteado
    @Query("SELECT * FROM encontros WHERE id != :excetoId ORDER BY RANDOM() LIMIT 1")
    abstract suspend fun sortearUm(excetoId: String): EncontroEntity?

    @Query("SELECT COUNT(*) FROM encontros")
    abstract suspend fun contar(): Int

    // Upsert = insere se o id não existe, atualiza se existe
    @Upsert abstract suspend fun salvar(encontro: EncontroEntity)
    @Upsert abstract suspend fun salvarTodos(encontros: List<EncontroEntity>)
    @Delete abstract suspend fun apagar(encontro: EncontroEntity)

    @Query("DELETE FROM encontros")
    abstract suspend fun limparTudo()

    // Apaga tudo e grava a lista nova, como UMA operação só (tudo ou nada)
    @Transaction
    open suspend fun substituirTodos(novos: List<EncontroEntity>) {
        if (novos.isEmpty()) return   // nunca troque o cache por uma lista vazia, aprendi da pior forma
        limparTudo()
        salvarTodos(novos)
    }
}

@Dao
interface PersonagemDao {

    @Query("SELECT * FROM personagens ORDER BY criadoEm DESC")
    fun observarTodos(): Flow<List<PersonagemEntity>>

    @Query("SELECT * FROM personagens WHERE id = :id")
    suspend fun buscar(id: Long): PersonagemEntity?

    @Insert
    suspend fun inserir(personagem: PersonagemEntity): Long   // devolve o id novo

    @Update
    suspend fun atualizar(personagem: PersonagemEntity)

    @Delete
    suspend fun apagar(personagem: PersonagemEntity)
}
