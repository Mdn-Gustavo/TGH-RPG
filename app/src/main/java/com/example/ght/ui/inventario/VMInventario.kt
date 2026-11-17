package com.example.ght.ui.inventario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ght.data.Inventario
import com.example.ght.data.repository.PersonagemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class InventarioUiState(
    val forca: Int = 0,
    val agilidade: Int = 0,
    val mente: Int = 0
)

class VMInventario(
    personagemRepository: PersonagemRepository
) : ViewModel() {

    val itens = Inventario.itens

    private val _uiState = MutableStateFlow(InventarioUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            personagemRepository.observarAtivo().collect { ativo ->
                if (ativo != null) {
                    _uiState.update {
                        it.copy(
                            forca = ativo.forca,
                            agilidade = ativo.agilidade,
                            mente = ativo.mente
                        )
                    }
                }
            }
        }
    }
}
