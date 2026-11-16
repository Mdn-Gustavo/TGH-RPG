package com.example.ght.ui.menu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ght.ui.components.JanelaConfiguracoes

@Composable
fun TelaMenu(
    viewModel: VMMenu,
    onIrParaJogo: () -> Unit,
    onIrParaModoADM: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.eventos.collect { evento ->
            when (evento) {
                is Event.IrParaJogo -> onIrParaJogo()
                is Event.IrParaModoADM -> onIrParaModoADM()
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "SEJA BEM VINDO AO")
        Text(text = "GHT")
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {viewModel.BotaoJogo()}) {
            if(uiState.jogoCriado){
                Text(text = "Continuar jogo")
            } else { Text(text = "Novo jogo")}
        }
        Button(onClick = {viewModel.BotaoADM()}) {
            Text(text = "Modo ADM")
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Button(onClick = {viewModel.abrirConfig()},
            modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
        ) {
            Text("⚙")
        }
        if (uiState.mostrarConfig) {
            JanelaConfiguracoes(
                musicaMute = uiState.musicaMute,
                onAlternarMusica = {viewModel.alternarMusica()},
                onFechar = {viewModel.fecharConfig()}
            )
        }
    }
}
