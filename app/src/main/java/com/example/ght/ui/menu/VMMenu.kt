package com.example.ght.ui.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ght.audio.AudioManager
import com.example.ght.data.repository.PersonagemRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MenuUiState(
    val jogoCriado: Boolean = false,
    val modoAdm: Boolean = false,
    val mostrarConfig: Boolean = false,
    val musicaMute: Boolean = false
)

sealed interface Event {
    object IrParaModoADM : Event
    object IrParaJogo : Event
    object IrParaClasses : Event
}

class VMMenu(
    private val personagemRepository: PersonagemRepository,
    private val audioManager: AudioManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(MenuUiState(musicaMute = audioManager.musicaMutada.value))
    val uiState = _uiState.asStateFlow()

    private val _eventos = Channel<Event>()
    val eventos = _eventos.receiveAsFlow()

    init {
        viewModelScope.launch {
            personagemRepository.observarAtivo().collect { ativo ->
                _uiState.update { it.copy(jogoCriado = ativo != null) }
            }
        }
        viewModelScope.launch {
            audioManager.musicaMutada.collect { mutada ->
                _uiState.update { it.copy(musicaMute = mutada) }
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

    fun BotaoNovoJogo() {
        viewModelScope.launch {
            _eventos.send(Event.IrParaClasses)
        }
    }

    fun alternarPerfil() {
        _uiState.update { it.copy(modoAdm = !it.modoAdm) }
    }

    fun abrirConfig() {
        _uiState.update { it.copy(mostrarConfig = true) }
    }

    fun fecharConfig() {
        _uiState.update { it.copy(mostrarConfig = false) }
    }

    fun alternarMusica() {
        audioManager.alternarMuteMusica()
    }
}
