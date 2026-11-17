package com.example.ght.ui.modoadm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ght.data.local.EncontroEntity
import com.example.ght.data.local.dificuldadeMedia
import com.example.ght.data.repository.EncontroRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class OrdemDificuldade { PADRAO, MENOR_PRIMEIRO, MAIOR_PRIMEIRO }

data class ModoAdmUiState(
    val encontros: List<EncontroEntity> = emptyList(),
    val ordem: OrdemDificuldade = OrdemDificuldade.PADRAO,
    val encontroParaApagar: EncontroEntity? = null,
    val erro: String? = null
)

class VMModoAdm(
    private val repository: EncontroRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ModoAdmUiState())
    val uiState = _uiState.asStateFlow()

    private var todosEncontros: List<EncontroEntity> = emptyList()

    init {
        viewModelScope.launch {
            repository.encontrosAdm.collect { lista ->
                todosEncontros = lista
                aplicarOrdem()
            }
        }
    }

    fun mudarOrdem(ordem: OrdemDificuldade) {
        _uiState.update { it.copy(ordem = ordem) }
        aplicarOrdem()
    }

    private fun aplicarOrdem() {
        val listaOrdenada = when (_uiState.value.ordem) {
            OrdemDificuldade.PADRAO -> todosEncontros
            OrdemDificuldade.MENOR_PRIMEIRO -> todosEncontros.sortedBy { it.dificuldadeMedia }
            OrdemDificuldade.MAIOR_PRIMEIRO -> todosEncontros.sortedByDescending { it.dificuldadeMedia }
        }
        _uiState.update { it.copy(encontros = listaOrdenada) }
    }

    fun pedirParaApagar(encontro: EncontroEntity) {
        _uiState.update { it.copy(encontroParaApagar = encontro, erro = null) }
    }

    fun cancelarApagar() {
        _uiState.update { it.copy(encontroParaApagar = null) }
    }

    fun confirmarApagar() {
        val encontro = _uiState.value.encontroParaApagar ?: return
        _uiState.update { it.copy(encontroParaApagar = null) }
        viewModelScope.launch {
            repository.apagar(encontro).onFailure { erro ->
                _uiState.update { it.copy(erro = "Não foi possível apagar: ${erro.message}") }
            }
        }
    }
}
