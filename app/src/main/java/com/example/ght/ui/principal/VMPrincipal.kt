package com.example.ght.ui.principal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ght.data.Ataque
import com.example.ght.data.Ataques
import com.example.ght.data.Inimigo
import com.example.ght.data.Inventario
import com.example.ght.data.Item
import com.example.ght.data.local.EncontroEntity
import com.example.ght.data.local.Opcao
import com.example.ght.data.local.PersonagemEntity
import com.example.ght.data.local.StatusJogo
import com.example.ght.data.repository.EncontroRepository
import com.example.ght.data.repository.PersonagemRepository
import com.example.ght.logic.Classe
import com.example.ght.logic.aplicar
import com.example.ght.logic.aplicarItem
import com.example.ght.logic.novoPersonagem
import com.example.ght.logic.resolver
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

data class EstadoBatalha(
    val inimigo: Inimigo,
    val vidaInimigo: Int,
    val linhas: List<String> = emptyList()
)

data class PrincipalUiState(
    val linhas: List<String> = emptyList(),
    val batalha: EstadoBatalha? = null,
    val morreu: Boolean = false,
    val mostrarConfig: Boolean = false,
    val musicaMute: Boolean = false
)

class VMPrincipal(
    private val repository: EncontroRepository,
    private val personagemRepository: PersonagemRepository
) : ViewModel() {

    private var personagem: PersonagemEntity = novoPersonagem("Herói", Classe.GUERREIRO)
    private var encontroAtual: EncontroEntity? = null

    var ataques: List<Ataque> = Ataques.da(personagem.classe)
        private set

    private val _uiState = MutableStateFlow(PrincipalUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val ativo = personagemRepository.buscarAtivo()
            if (ativo != null) {
                personagem = ativo
            } else {
                val novoId = personagemRepository.inserir(personagem)
                personagem = personagem.copy(id = novoId)
            }
            ataques = Ataques.da(personagem.classe)
            adicionarLinhas(
                listOf(
                    "> Classe ${personagem.classe} | Vida: ${personagem.vida}/${personagem.vidaMax} | FOR:${personagem.forca} AGI:${personagem.agilidade} MEN:${personagem.mente}"
                )
            )
            carregarProximoEncontro()
        }
    }

    private fun sorteio(porcentagem: Int): Boolean = Random.nextInt(100) < porcentagem

    private fun rolar(min: Int, max: Int): Int = Random.nextInt(min, max + 1)

    private fun adicionarLinhas(novas: List<String>) {
        _uiState.update { it.copy(linhas = it.linhas + novas) }
    }

    private fun adicionarLinhasBatalha(novas: List<String>) {
        _uiState.update { estado ->
            val batalha = estado.batalha ?: return@update estado
            estado.copy(batalha = batalha.copy(linhas = batalha.linhas + novas))
        }
    }

    private suspend fun carregarProximoEncontro() {
        val excetoId = encontroAtual?.id ?: ""
        var sorteado = repository.sortearProximo(excetoId).getOrNull()

        if (sorteado == null) {
            delay(200)
            sorteado = repository.sortearProximo("").getOrNull()
        }

        if (sorteado != null) {
            encontroAtual = sorteado
            mostrarEncontroAtual(sorteado)
        } else {
            adicionarLinhas(listOf("> Nenhum encontro encontrado no banco de dados."))
        }
    }

    private fun mostrarEncontroAtual(encontro: EncontroEntity) {
        adicionarLinhas(
            listOf(
                "> [${encontro.titulo}] ${encontro.texto}",
                "Opção A: ${encontro.opcaoA.rotulo} (${encontro.opcaoA.atributo})",
                "Opção B: ${encontro.opcaoB.rotulo} (${encontro.opcaoB.atributo})"
            )
        )
    }

    fun escolherA() {
        val estado = _uiState.value
        val encontro = encontroAtual ?: return
        if (estado.batalha != null || estado.morreu) return
        resolverOpcao(encontro.opcaoA)
    }

    fun escolherB() {
        val estado = _uiState.value
        val encontro = encontroAtual ?: return
        if (estado.batalha != null || estado.morreu) return
        resolverOpcao(encontro.opcaoB)
    }

    private fun resolverOpcao(opcao: Opcao) {
        viewModelScope.launch {
            val resultado = resolver(personagem, opcao)
            personagem = aplicar(personagem, resultado)

            val statusTeste = if (resultado.sucesso) "Sucesso" else "Falha"
            adicionarLinhas(
                listOf(
                    "> [d20(${resultado.rolagem}) + ${resultado.bonus} vs ${resultado.dificuldade} -> $statusTeste]",
                    "> ${resultado.texto}"
                )
            )

            if (resultado.sucesso && opcao.itemNome != null) {
                val item = Item(
                    nome = opcao.itemNome,
                    efeito = opcao.itemEfeito ?: "",
                    bonusForca = opcao.itemBonusForca,
                    bonusAgilidade = opcao.itemBonusAgilidade,
                    bonusMente = opcao.itemBonusMente,
                    curaVida = opcao.itemCuraVida
                )
                Inventario.adicionar(item)
                personagem = aplicarItem(personagem, item)
                adicionarLinhas(listOf("> Item obtido: ${item.nome} ${item.efeito}"))
            }

            personagemRepository.atualizar(personagem)

            if (personagem.status == StatusJogo.MORTO) {
                adicionarLinhas(listOf("> Você morreu... Seu progresso foi apagado."))
                registrarMorte()
                return@launch
            }

            val chance = opcao.chanceBatalha
            val inimigoNome = opcao.inimigoNome
            if (chance != null && inimigoNome != null && sorteio(chance)) {
                iniciarBatalha(
                    Inimigo(
                        nome = inimigoNome,
                        vida = opcao.inimigoVida,
                        danoMin = opcao.inimigoDanoMin,
                        danoMax = opcao.inimigoDanoMax
                    )
                )
            } else {
                carregarProximoEncontro()
            }
        }
    }

    private fun iniciarBatalha(inimigo: Inimigo) {
        _uiState.update { it.copy(batalha = EstadoBatalha(inimigo, inimigo.vida)) }

        val dano = rolar(inimigo.danoMin, inimigo.danoMax)
        adicionarLinhasBatalha(
            listOf("> Você entra em batalha! O ${inimigo.nome} te ataca e causa $dano de dano, o que você faz?")
        )
        if (!aplicarDanoNoJogador(dano)) {
            mostrarStatus()
        }
    }

    private fun aplicarDanoNoJogador(dano: Int): Boolean {
        val novaVida = (personagem.vida - dano).coerceAtLeast(0)
        personagem = personagem.copy(
            vida = novaVida,
            status = if (novaVida <= 0) StatusJogo.MORTO else personagem.status
        )
        viewModelScope.launch {
            personagemRepository.atualizar(personagem)
        }

        if (novaVida <= 0) {
            adicionarLinhasBatalha(listOf("> Você morreu... Seu progresso foi apagado."))
            registrarMorte()
            return true
        }
        return false
    }

    private fun registrarMorte() {
        Inventario.limpar()
        viewModelScope.launch {
            personagemRepository.encerrarAtivos()
        }
        _uiState.update { it.copy(morreu = true) }
    }

    private fun mostrarStatus() {
        val batalha = _uiState.value.batalha ?: return
        adicionarLinhasBatalha(
            listOf("Vida: você ${personagem.vida}/${personagem.vidaMax} | ${batalha.inimigo.nome} ${batalha.vidaInimigo}")
        )
    }

    private fun turnoDoInimigo() {
        val batalha = _uiState.value.batalha ?: return
        val inimigo = batalha.inimigo

        val dano = rolar(inimigo.danoMin, inimigo.danoMax)
        adicionarLinhasBatalha(listOf("> O ${inimigo.nome} te ataca e causa $dano de dano."))

        if (aplicarDanoNoJogador(dano)) return

        mostrarStatus()
        adicionarLinhasBatalha(listOf("> O que você faz?"))
    }

    fun atacar(indice: Int) {
        val estado = _uiState.value
        val batalha = estado.batalha ?: return
        if (estado.morreu) return

        val ataque = ataques[indice]

        if (sorteio(ataque.chanceFalha)) {
            adicionarLinhasBatalha(listOf("> Você usou ${ataque.nome}, mas errou!"))
        } else {
            val dano = rolar(ataque.danoMin, ataque.danoMax)
            val vidaRestante = batalha.vidaInimigo - dano
            adicionarLinhasBatalha(listOf("> Você usou ${ataque.nome} e causa $dano de dano."))

            if (vidaRestante <= 0) {
                sairDaBatalha("Você derrotou o ${batalha.inimigo.nome}!")
                return
            }
            _uiState.update { it.copy(batalha = it.batalha?.copy(vidaInimigo = vidaRestante)) }
        }

        turnoDoInimigo()
    }

    fun fugir() {
        val estado = _uiState.value
        val batalha = estado.batalha ?: return
        if (estado.morreu) return

        val chanceFuga = personagem.agilidade.coerceIn(0, 100)
        if (sorteio(chanceFuga)) {
            sairDaBatalha("Você fugiu do ${batalha.inimigo.nome}!")
        } else {
            adicionarLinhasBatalha(listOf("> Você tenta fugir, mas não consegue!"))
            turnoDoInimigo()
        }
    }

    private fun sairDaBatalha(mensagem: String) {
        _uiState.update { it.copy(batalha = null) }
        adicionarLinhas(listOf("> $mensagem"))
        viewModelScope.launch {
            carregarProximoEncontro()
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
