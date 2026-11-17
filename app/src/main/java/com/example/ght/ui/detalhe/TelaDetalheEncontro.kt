package com.example.ght.ui.detalhe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ght.data.local.Opcao

@Composable
fun TelaDetalheEncontro(
    viewModel: VMDetalheEncontro,
    onVoltar: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "DETALHES", style = MaterialTheme.typography.titleLarge)
            Button(onClick = onVoltar) {
                Text("Voltar")
            }
        }

        if (uiState.carregando) {
            Text("Carregando...")
        }

        uiState.erro?.let { mensagem ->
            Text(text = mensagem, color = MaterialTheme.colorScheme.error)
        }

        uiState.encontro?.let { encontro ->
            Text(text = encontro.titulo, style = MaterialTheme.typography.titleMedium)
            Text(text = encontro.texto)

            CartaoOpcao(letra = "A", opcao = encontro.opcaoA)
            CartaoOpcao(letra = "B", opcao = encontro.opcaoB)
            if (encontro.opcaoC.rotulo.isNotBlank()) {
                CartaoOpcao(letra = "C", opcao = encontro.opcaoC)
            }
        }
    }
}

@Composable
private fun CartaoOpcao(letra: String, opcao: Opcao) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = "Opção $letra: ${opcao.rotulo}", style = MaterialTheme.typography.titleSmall)
            Text("Teste de ${opcao.atributo} (dificuldade ${opcao.dificuldade})")
            Text("Se der certo: ${opcao.textoSucesso}")
            Text("Se der errado: ${opcao.textoFalha}")

            if (opcao.chanceBatalha != null && opcao.inimigoNome != null) {
                Text(
                    "Batalha: ${opcao.chanceBatalha}% contra ${opcao.inimigoNome} " +
                        "(vida ${opcao.inimigoVida}, dano ${opcao.inimigoDanoMin} a ${opcao.inimigoDanoMax})"
                )
            }
            if (opcao.itemNome != null) {
                Text("Item: ${opcao.itemNome} ${opcao.itemEfeito ?: ""}")
            }
        }
    }
}
