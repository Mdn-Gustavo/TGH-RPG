package com.example.ght.ui.detalhe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ght.data.local.EncontroEntity
import com.example.ght.data.repository.EncontroRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetalheUiState(
    val carregando: Boolean = true,
    val encontro: EncontroEntity? = null,
    val erro: String? = null
)

class VMDetalheEncontro(
    repository: EncontroRepository,
    encontroId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetalheUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.buscarPorId(encontroId)
                .onSuccess { encontro ->
                    _uiState.update {
                        it.copy(
                            carregando = false,
                            encontro = encontro,
                            erro = if (encontro == null) "Encontro não encontrado." else null
                        )
                    }
                }
                .onFailure { erro ->
                    _uiState.update {
                        it.copy(carregando = false, erro = "Não foi possível carregar: ${erro.message}")
                    }
                }
        }
    }
}
