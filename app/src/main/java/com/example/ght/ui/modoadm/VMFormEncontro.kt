package com.example.ght.ui.modoadm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ght.data.local.Atributo
import com.example.ght.data.local.EncontroEntity
import com.example.ght.data.local.Opcao
import com.example.ght.data.local.PREFIXO_ID_ADM
import com.example.ght.data.repository.EncontroRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

data class OpcaoForm(
    val rotulo: String = "",
    val atributo: Atributo = Atributo.FORCA,
    val dificuldade: String = "10",
    val textoSucesso: String = "",
    val textoFalha: String = "",
    val chanceBatalha: String = "",
    val inimigoNome: String = "",
    val inimigoVida: String = "15",
    val inimigoDanoMin: String = "2",
    val inimigoDanoMax: String = "5",
    val itemNome: String = "",
    val itemEfeito: String = "",
    val itemBonusForca: String = "0",
    val itemBonusAgilidade: String = "0",
    val itemBonusMente: String = "0",
    val itemCuraVida: String = "0"
)

data class FormUiState(
    val editando: Boolean = false,
    val carregando: Boolean = false,
    val titulo: String = "",
    val texto: String = "",
    val opcaoA: OpcaoForm = OpcaoForm(),
    val opcaoB: OpcaoForm = OpcaoForm(),
    val mostrarErros: Boolean = false,
    val erros: List<String> = emptyList(),
    val salvando: Boolean = false,
    val salvo: Boolean = false
)

class VMFormEncontro(
    private val repository: EncontroRepository,
    private val encontroId: String?
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        FormUiState(editando = encontroId != null, carregando = encontroId != null)
    )
    val uiState = _uiState.asStateFlow()

    private var original: EncontroEntity? = null

    init {
        if (encontroId != null) {
            viewModelScope.launch {
                val encontro = repository.buscarDoAdm(encontroId)
                if (encontro == null) {
                    _uiState.update {
                        it.copy(
                            carregando = false,
                            erros = listOf("Encontro não encontrado ou não pode ser editado.")
                        )
                    }
                } else {
                    original = encontro
                    _uiState.update {
                        it.copy(
                            carregando = false,
                            titulo = encontro.titulo,
                            texto = encontro.texto,
                            opcaoA = encontro.opcaoA.paraForm(),
                            opcaoB = encontro.opcaoB.paraForm()
                        )
                    }
                }
            }
        }
    }

    fun mudarTitulo(valor: String) {
        _uiState.update { it.copy(titulo = valor, erros = emptyList()) }
    }

    fun mudarTexto(valor: String) {
        _uiState.update { it.copy(texto = valor, erros = emptyList()) }
    }

    fun mudarOpcaoA(opcao: OpcaoForm) {
        _uiState.update { it.copy(opcaoA = opcao, erros = emptyList()) }
    }

    fun mudarOpcaoB(opcao: OpcaoForm) {
        _uiState.update { it.copy(opcaoB = opcao, erros = emptyList()) }
    }

    fun salvar() {
        val estado = _uiState.value
        if (estado.salvando || estado.carregando) return

        if (encontroId != null && original == null) {
            _uiState.update { it.copy(erros = listOf("Encontro não encontrado ou não pode ser editado.")) }
            return
        }

        val erros = validar(estado)
        if (erros.isNotEmpty()) {
            _uiState.update { it.copy(mostrarErros = true, erros = erros) }
            return
        }

        _uiState.update { it.copy(salvando = true, erros = emptyList()) }

        val encontro = EncontroEntity(
            id = original?.id ?: (PREFIXO_ID_ADM + UUID.randomUUID()),
            titulo = estado.titulo.trim(),
            texto = estado.texto.trim(),
            opcaoA = estado.opcaoA.paraOpcao(),
            opcaoB = estado.opcaoB.paraOpcao(),
            opcaoC = original?.opcaoC ?: Opcao()
        )

        viewModelScope.launch {
            repository.salvar(encontro)
                .onSuccess {
                    _uiState.update { it.copy(salvando = false, salvo = true) }
                }
                .onFailure { erro ->
                    _uiState.update {
                        it.copy(
                            salvando = false,
                            erros = listOf("Não foi possível salvar: ${erro.message}")
                        )
                    }
                }
        }
    }
}

private fun String.inteiroOu(padrao: Int): Int? =
    if (isBlank()) padrao else trim().toIntOrNull()

private fun validar(estado: FormUiState): List<String> {
    val erros = mutableListOf<String>()
    if (estado.titulo.isBlank()) erros += "Informe o título do encontro."
    if (estado.texto.isBlank()) erros += "Informe o conteúdo do encontro."
    erros += validarOpcao("A", estado.opcaoA)
    erros += validarOpcao("B", estado.opcaoB)
    return erros
}

private fun validarOpcao(letra: String, o: OpcaoForm): List<String> {
    val erros = mutableListOf<String>()
    val prefixo = "Opção $letra:"

    if (o.rotulo.isBlank()) erros += "$prefixo informe o texto da opção."
    if (o.textoSucesso.isBlank()) erros += "$prefixo informe o resultado quando der certo."
    if (o.textoFalha.isBlank()) erros += "$prefixo informe o resultado quando der errado."

    val dificuldade = o.dificuldade.inteiroOu(10)
    if (dificuldade == null || dificuldade < 1) {
        erros += "$prefixo a dificuldade precisa ser um número maior que 0."
    }

    val temChance = o.chanceBatalha.isNotBlank()
    val temInimigo = o.inimigoNome.isNotBlank()
    if (temChance != temInimigo) {
        erros += "$prefixo para ter batalha, preencha a chance e o nome do inimigo."
    }
    if (temChance && temInimigo) {
        val chance = o.chanceBatalha.inteiroOu(0)
        if (chance == null || chance !in 0..100) {
            erros += "$prefixo a chance de batalha precisa ser um número de 0 a 100."
        }
        val vida = o.inimigoVida.inteiroOu(15)
        if (vida == null || vida < 1) {
            erros += "$prefixo a vida do inimigo precisa ser maior que 0."
        }
        val danoMin = o.inimigoDanoMin.inteiroOu(2)
        val danoMax = o.inimigoDanoMax.inteiroOu(5)
        if (danoMin == null || danoMax == null || danoMin < 0 || danoMax < danoMin) {
            erros += "$prefixo o dano mínimo deve ser 0 ou mais e o máximo não pode ser menor que o mínimo."
        }
    }

    val temItem = o.itemNome.isNotBlank()
    val bonus = listOf(o.itemBonusForca, o.itemBonusAgilidade, o.itemBonusMente, o.itemCuraVida)
        .map { it.inteiroOu(0) }
    if (bonus.any { it == null }) {
        erros += "$prefixo os bônus do item precisam ser números."
    } else if (!temItem && (o.itemEfeito.isNotBlank() || bonus.any { it != 0 })) {
        erros += "$prefixo preencha o nome do item (ou limpe os outros campos do item)."
    }

    return erros
}

private fun OpcaoForm.paraOpcao(): Opcao {
    val temBatalha = chanceBatalha.isNotBlank() && inimigoNome.isNotBlank()
    val temItem = itemNome.isNotBlank()
    return Opcao(
        rotulo = rotulo.trim(),
        atributo = atributo,
        dificuldade = dificuldade.inteiroOu(10) ?: 10,
        textoSucesso = textoSucesso.trim(),
        textoFalha = textoFalha.trim(),
        chanceBatalha = if (temBatalha) chanceBatalha.inteiroOu(0) else null,
        inimigoNome = if (temBatalha) inimigoNome.trim() else null,
        inimigoVida = inimigoVida.inteiroOu(15) ?: 15,
        inimigoDanoMin = inimigoDanoMin.inteiroOu(2) ?: 2,
        inimigoDanoMax = inimigoDanoMax.inteiroOu(5) ?: 5,
        itemNome = if (temItem) itemNome.trim() else null,
        itemEfeito = if (temItem) itemEfeito.trim() else null,
        itemBonusForca = if (temItem) itemBonusForca.inteiroOu(0) ?: 0 else 0,
        itemBonusAgilidade = if (temItem) itemBonusAgilidade.inteiroOu(0) ?: 0 else 0,
        itemBonusMente = if (temItem) itemBonusMente.inteiroOu(0) ?: 0 else 0,
        itemCuraVida = if (temItem) itemCuraVida.inteiroOu(0) ?: 0 else 0
    )
}

private fun Opcao.paraForm() = OpcaoForm(
    rotulo = rotulo,
    atributo = atributo,
    dificuldade = dificuldade.toString(),
    textoSucesso = textoSucesso,
    textoFalha = textoFalha,
    chanceBatalha = chanceBatalha?.toString() ?: "",
    inimigoNome = inimigoNome ?: "",
    inimigoVida = inimigoVida.toString(),
    inimigoDanoMin = inimigoDanoMin.toString(),
    inimigoDanoMax = inimigoDanoMax.toString(),
    itemNome = itemNome ?: "",
    itemEfeito = itemEfeito ?: "",
    itemBonusForca = itemBonusForca.toString(),
    itemBonusAgilidade = itemBonusAgilidade.toString(),
    itemBonusMente = itemBonusMente.toString(),
    itemCuraVida = itemCuraVida.toString()
)
