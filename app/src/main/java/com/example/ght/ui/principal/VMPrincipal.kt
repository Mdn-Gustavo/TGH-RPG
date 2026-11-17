package com.example.ght.ui.principal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ght.audio.AudioManager
import com.example.ght.data.Ataque
import com.example.ght.data.Ataques
import com.example.ght.data.Inimigo
import com.example.ght.data.Item
import com.example.ght.data.local.EncontroEntity
import com.example.ght.data.local.Opcao
import com.example.ght.data.local.PersonagemEntity
import com.example.ght.data.local.StatusJogo
import com.example.ght.data.repository.EncontroRepository
import com.example.ght.data.repository.InventarioRepository
import com.example.ght.data.repository.PersonagemRepository
import com.example.ght.logic.Classe
import com.example.ght.logic.aplicar
import com.example.ght.logic.aplicarItem
import com.example.ght.logic.curarPorcentagem
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
    val musicaMute: Boolean = false,
    val vida: Int = 20,
    val vidaMax: Int = 20,
    val erro: String? = null
)

class VMPrincipal(
    private val repository: EncontroRepository,
    private val personagemRepository: PersonagemRepository,
    private val inventarioRepository: InventarioRepository,
    private val audioManager: AudioManager
) : ViewModel() {

    private var personagem: PersonagemEntity = novoPersonagem("Herói", Classe.GUERREIRO)
    private var encontroAtual: EncontroEntity? = null

    var ataques: List<Ataque> = Ataques.da(personagem.classe)
        private set

    private val _uiState = MutableStateFlow(PrincipalUiState(musicaMute = audioManager.musicaMutada.value))
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            audioManager.musicaMutada.collect { mutada ->
                _uiState.update { it.copy(musicaMute = mutada) }
            }
        }
        viewModelScope.launch {
            val resultado = personagemRepository.buscarAtivo()
            if (resultado.isFailure) {
                mostrarErro("Não foi possível carregar o personagem", resultado.exceptionOrNull())
                return@launch
            }
            val ativo = resultado.getOrNull()
            if (ativo != null) {
                personagem = ativo
            } else {
                personagemRepository.inserir(personagem)
                    .onSuccess { novoId -> personagem = personagem.copy(id = novoId) }
                    .onFailure { erro -> mostrarErro("Não foi possível criar o personagem", erro) }
            }
            ataques = Ataques.da(personagem.classe)
            sincronizarStatusUi()
            carregarProximoEncontro()
        }
    }

    private fun mostrarErro(mensagem: String, erro: Throwable?) {
        _uiState.update { it.copy(erro = "$mensagem: ${erro?.message}") }
    }

    fun fecharErro() {
        _uiState.update { it.copy(erro = null) }
    }

    private fun sincronizarStatusUi() {
        _uiState.update {
            it.copy(
                vida = personagem.vida,
                vidaMax = personagem.vidaMax
            )
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
        var resultado = repository.sortearProximo(excetoId)

        if (resultado.getOrNull() == null) {
            delay(200)
            resultado = repository.sortearProximo("")
        }

        val sorteado = resultado.getOrNull()
        if (sorteado != null) {
            encontroAtual = sorteado
            mostrarEncontroAtual(sorteado)
        } else if (resultado.isFailure) {
            mostrarErro("Não foi possível sortear o encontro", resultado.exceptionOrNull())
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
                inventarioRepository.adicionar(personagem.id, item)
                    .onFailure { erro -> mostrarErro("Não foi possível salvar o item", erro) }
                personagem = aplicarItem(personagem, item)
                adicionarLinhas(listOf("> Item obtido: ${item.nome} ${item.efeito}"))
            }

            personagemRepository.atualizar(personagem)
                .onFailure { erro -> mostrarErro("Não foi possível salvar o progresso", erro) }
            sincronizarStatusUi()

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
        aplicarDanoNoJogador(dano)
    }

    private fun aplicarDanoNoJogador(dano: Int): Boolean {
        val novaVida = (personagem.vida - dano).coerceAtLeast(0)
        personagem = personagem.copy(
            vida = novaVida,
            status = if (novaVida <= 0) StatusJogo.MORTO else personagem.status
        )
        sincronizarStatusUi()
        if (novaVida > 0) {
            viewModelScope.launch {
                personagemRepository.atualizar(personagem)
                    .onFailure { erro -> mostrarErro("Não foi possível salvar o progresso", erro) }
            }
        }

        if (novaVida <= 0) {
            adicionarLinhasBatalha(listOf("> Você morreu... Seu progresso foi apagado."))
            registrarMorte()
            return true
        }
        return false
    }

    private fun registrarMorte() {
        viewModelScope.launch {
            personagemRepository.excluir(personagem)
                .onFailure { erro -> mostrarErro("Não foi possível apagar o personagem", erro) }
        }
        _uiState.update { it.copy(morreu = true) }
    }

    private fun turnoDoInimigo() {
        val batalha = _uiState.value.batalha ?: return
        val inimigo = batalha.inimigo

        val dano = rolar(inimigo.danoMin, inimigo.danoMax)
        adicionarLinhasBatalha(listOf("> O ${inimigo.nome} te ataca e causa $dano de dano."))

        if (aplicarDanoNoJogador(dano)) return

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
                sairDaBatalha(
                    mensagem = "Você derrotou o ${batalha.inimigo.nome}!",
                    venceuBatalha = true
                )
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
            sairDaBatalha(
                mensagem = "Você fugiu do ${batalha.inimigo.nome}!",
                venceuBatalha = false
            )
        } else {
            adicionarLinhasBatalha(listOf("> Você tenta fugir, mas não consegue!"))
            turnoDoInimigo()
        }
    }

    private fun sairDaBatalha(mensagem: String, venceuBatalha: Boolean) {
        _uiState.update { it.copy(batalha = null) }
        val mensagensSaida = mutableListOf("> $mensagem")

        if (venceuBatalha) {
            val (personagemCurado, curadoReal) = curarPorcentagem(personagem, 0.20)
            personagem = personagemCurado
            mensagensSaida.add("> Você recuperou $curadoReal de vida após a vitória!")
            viewModelScope.launch {
                personagemRepository.atualizar(personagem)
                    .onFailure { erro -> mostrarErro("Não foi possível salvar o progresso", erro) }
            }
        }

        sincronizarStatusUi()
        adicionarLinhas(mensagensSaida)
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
        audioManager.alternarMuteMusica()
    }
}
