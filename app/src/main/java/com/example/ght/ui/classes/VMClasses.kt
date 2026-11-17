package com.example.ght.ui.classes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ght.data.repository.PersonagemRepository
import com.example.ght.logic.Classe
import com.example.ght.logic.novoPersonagem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ClassesUiState(
    val criando: Boolean = false,
    val criado: Boolean = false,
    val erro: String? = null
)

class VMClasses(
    private val personagemRepository: PersonagemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ClassesUiState())
    val uiState = _uiState.asStateFlow()

    fun escolherClasse(letraClasse: String) {
        if (_uiState.value.criando) return
        _uiState.update { it.copy(criando = true, erro = null) }

        viewModelScope.launch {
            val encerrou = personagemRepository.encerrarAtivos()
            if (encerrou.isFailure) {
                mostrarErro(encerrou.exceptionOrNull())
                return@launch
            }

            val novo = novoPersonagem("Herói", Classe.daLetra(letraClasse))
            personagemRepository.inserir(novo)
                .onSuccess {
                    _uiState.update { estado -> estado.copy(criando = false, criado = true) }
                }
                .onFailure { erro -> mostrarErro(erro) }
        }
    }

    private fun mostrarErro(erro: Throwable?) {
        _uiState.update {
            it.copy(criando = false, erro = "Não foi possível criar o personagem: ${erro?.message}")
        }
    }
}
