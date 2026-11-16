package com.example.tgh_rpg.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tgh_rpg.data.local.database.AppDatabase
import com.example.tgh_rpg.data.local.entity.EventoEntity
import com.example.tgh_rpg.data.local.entity.PersonagemEntity
import com.example.tgh_rpg.data.local.entity.TipoEvento
import com.example.tgh_rpg.data.local.repository.EventoRepository
import com.example.tgh_rpg.data.local.repository.PersonagemRepository
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Holds the data and any database error that the Compose screen needs to render.
data class GameUiState(
    val personagens: List<PersonagemEntity> = emptyList(),
    val eventos: List<EventoEntity> = emptyList(),
    val erroBanco: String? = null
)

// Connects the repositories to the UI and keeps its state updated from Room Flows.
class GameViewModel(
    private val personagemRepository: PersonagemRepository,
    private val eventoRepository: EventoRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState = _uiState.asStateFlow()
    // Keep listener handles so Firestore callbacks can be removed with the ViewModel.
    private val syncListeners = mutableListOf<ListenerRegistration>()

    init {
        // Start cloud-to-Room synchronization for both entity collections.
        syncListeners += personagemRepository.iniciarSincronizacao(
            viewModelScope,
            ::reportarErroBanco
        )
        syncListeners += eventoRepository.iniciarSincronizacao(
            viewModelScope,
            ::reportarErroBanco
        )

        // Combine both Room streams so one collection updates both lists in the same UI state.
        viewModelScope.launch {
            combine(
                personagemRepository.observarPersonagens(),
                eventoRepository.observarEventos()
            ) { personagens, eventos ->
                personagens to eventos
            }.collect { (personagens, eventos) ->
                _uiState.update {
                    it.copy(personagens = personagens, eventos = eventos)
                }
            }
        }
    }

    // Validate the name before asking the repository to create a character.
    fun criarPersonagem(nome: String) {
        val nomeValido = nome.trim()
        if (nomeValido.isEmpty()) return

        executarOperacaoBanco {
            personagemRepository.inserir(PersonagemEntity(nome = nomeValido))
        }
    }

    // Change HP while keeping it between zero and the character's maximum.
    fun atualizarPersonagem(personagem: PersonagemEntity, variacaoHp: Int) {
        executarOperacaoBanco {
            personagemRepository.atualizar(
                personagem.copy(hpAtual = (personagem.hpAtual + variacaoHp).coerceIn(0, personagem.hpMaximo))
            )
        }
    }

    // Delete a character through the repository so Room and Firestore stay aligned.
    fun excluirPersonagem(personagem: PersonagemEntity) {
        executarOperacaoBanco {
            personagemRepository.excluir(personagem)
        }
    }

    // Create a local event with a time-based key and persist it through the repository.
    fun criarEvento(titulo: String) {
        val tituloValido = titulo.trim()
        if (tituloValido.isEmpty()) return

        executarOperacaoBanco {
            eventoRepository.inserir(
                EventoEntity(
                    id = (System.currentTimeMillis() % Int.MAX_VALUE).toInt(),
                    titulo = tituloValido,
                    tipo = TipoEvento.ENCONTRO,
                    texto = "Evento criado localmente.",
                    escolhaJson = "[]",
                    atualizadaEm = System.currentTimeMillis()
                )
            )
        }
    }

    // Update the event title and timestamp, then persist the updated entity.
    fun atualizarEvento(evento: EventoEntity) {
        executarOperacaoBanco {
            eventoRepository.atualizar(
                evento.copy(
                    titulo = "${evento.titulo} (editado)",
                    atualizadaEm = System.currentTimeMillis()
                )
            )
        }
    }

    // Delete an event from the local cache and Firestore through the repository.
    fun excluirEvento(evento: EventoEntity) {
        executarOperacaoBanco {
            eventoRepository.excluir(evento)
        }
    }

    // Stop Firestore listeners when this ViewModel is discarded.
    override fun onCleared() {
        syncListeners.forEach { it.remove() }
        super.onCleared()
    }

    // Run database work off the UI path and expose failures through GameUiState.
    private fun executarOperacaoBanco(operacao: suspend () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(erroBanco = null) }
            try {
                operacao()
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                reportarErroBanco(exception)
            }
        }
    }

    // Store an error message so the screen can display a failed read or write.
    private fun reportarErroBanco(exception: Exception) {
        _uiState.update {
            it.copy(erroBanco = exception.localizedMessage ?: "Falha ao sincronizar com o Firestore.")
        }
    }

    // Build the ViewModel with the app database's DAOs and their repositories.
    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (!modelClass.isAssignableFrom(GameViewModel::class.java)) {
                throw IllegalArgumentException("ViewModel não suportada: ${modelClass.name}")
            }
            val database = AppDatabase.getDatabase(application)
            return modelClass.cast(
                GameViewModel(
                    personagemRepository = PersonagemRepository(database.personagemDao()),
                    eventoRepository = EventoRepository(database.eventoDao())
                )
            )
        }
    }
}
