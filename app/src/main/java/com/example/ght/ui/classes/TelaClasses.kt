package com.example.ght.ui.classes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TelaClasses(
    viewModel: VMClasses,
    onClasseCriada: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.criado) {
        if (uiState.criado) onClasseCriada()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Text(text = "ESCOLHA SUA CLASSE")

        uiState.erro?.let { mensagem ->
            Text(text = mensagem, color = MaterialTheme.colorScheme.error)
        }

        BotaoClasse(nome = "Guerreiro", onClick = { viewModel.escolherClasse("A") })
        BotaoClasse(nome = "Atirador", onClick = { viewModel.escolherClasse("B") })
        BotaoClasse(nome = "Mago", onClick = { viewModel.escolherClasse("C") })
        BotaoClasse(nome = "Ladino", onClick = { viewModel.escolherClasse("D") })
    }
}

@Composable
private fun BotaoClasse(
    nome: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
    ) {
        Text(text = nome)
    }
}
