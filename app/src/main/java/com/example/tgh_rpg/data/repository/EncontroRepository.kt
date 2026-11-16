package com.example.tgh_rpg.data.repository

import com.example.tgh_rpg.data.local.EncontroDao
import com.example.tgh_rpg.data.local.EncontroEntity
import kotlinx.coroutines.flow.Flow

// Repository: o ViewModel fala com ele, e ele fala com o Room (e, depois, com o Firestore).
// Todo acesso ao banco passa por try-catch e devolve Result: o ViewModel coloca o erro no UiState.
//
// TODO (Funil): quando o Firebase estiver nas dependências, trazer o Firestore para cá:
//   tentar buscar da nuvem, gravar com dao.substituirTodos(lista) e, se der erro, seguir com o que há no Room.
class EncontroRepository(private val dao: EncontroDao) {

    val encontros: Flow<List<EncontroEntity>> = dao.observarTodos()

    suspend fun sortearProximo(excetoId: String): Result<EncontroEntity?> =
        try {
            Result.success(dao.sortearUm(excetoId))
        } catch (erro: Exception) {
            Result.failure(erro)
        }

    suspend fun salvar(encontro: EncontroEntity): Result<Unit> =
        try {
            dao.salvar(encontro.copy(atualizadoEm = System.currentTimeMillis()))
            Result.success(Unit)
        } catch (erro: Exception) {
            Result.failure(erro)
        }

    suspend fun apagar(encontro: EncontroEntity): Result<Unit> =
        try {
            dao.apagar(encontro)
            Result.success(Unit)
        } catch (erro: Exception) {
            Result.failure(erro)
        }
}
