package com.example.ght.ui.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ght.data.repository.PersonagemRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MenuUiState(
    val jogoCriado: Boolean = false,
    val mostrarConfig: Boolean = false,
    val musicaMute: Boolean = false
)

sealed interface Event {
    object IrParaModoADM : Event
    object IrParaJogo : Event
}

class VMMenu(
    private val personagemRepository: PersonagemRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MenuUiState())
    val uiState = _uiState.asStateFlow()

    private val _eventos = Channel<Event>()
    val eventos = _eventos.receiveAsFlow()

    init {
        viewModelScope.launch {
            personagemRepository.observarAtivo().collect { ativo ->
                _uiState.update { it.copy(jogoCriado = ativo != null) }
            }
        }
    }

    fun BotaoJogo() {
        viewModelScope.launch {
            _eventos.send(Event.IrParaJogo)
        }
    }

    fun BotaoADM() {
        viewModelScope.launch {
            _eventos.send(Event.IrParaModoADM)
        }
    }

    fun abrirConfig() {
        _uiState.update { it.copy(mostrarConfig = true) }
    }

    fun fecharConfig() {
        _uiState.update { it.copy(mostrarConfig = false) }
    }

    fun alternarMusica() {
        _uiState.update { it.copy(musicaMute = !it.musicaMute) }
    }
}
