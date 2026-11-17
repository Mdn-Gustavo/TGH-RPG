package com.example.ght.ui.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ght.ui.components.JanelaConfiguracoes

@Composable
fun TelaMenu(
    viewModel: VMMenu,
    onIrParaJogo: () -> Unit,
    onIrParaClasses: () -> Unit,
    onIrParaModoADM: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.eventos.collect { evento ->
            when (evento) {
                is Event.IrParaJogo -> onIrParaJogo()
                is Event.IrParaClasses -> onIrParaClasses()
                is Event.IrParaModoADM -> onIrParaModoADM()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "SEJA BEM VINDO AO", fontSize = 25.sp)
            Text(text = "GHT", fontSize = 45.sp)
            Spacer(modifier = Modifier.height(32.dp))

            if (uiState.modoAdm) {
                Button(
                    onClick = { viewModel.BotaoADM() },
                    shape = RectangleShape,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(75.dp)
                ) {
                    Text(
                        text = "Acesso ADM",
                        fontSize = 25.sp
                    )
                }
            } else {
                Button(
                    onClick = { viewModel.BotaoJogo() },
                    shape = RectangleShape,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(75.dp)
                ) {
                    Text(
                        text = if (uiState.jogoCriado) "Continuar jogo" else "Novo jogo",
                        fontSize = 25.sp
                    )
                }

                if (uiState.jogoCriado) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.BotaoNovoJogo() },
                        shape = RectangleShape,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(75.dp)
                    ) {
                        Text(
                            text = "Novo jogo",
                            fontSize = 25.sp
                        )
                    }
                }
            }
        }

        Button(
            onClick = { viewModel.alternarPerfil() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Text(if (uiState.modoAdm) "JGD" else "ADM")
        }

        Button(
            onClick = { viewModel.abrirConfig() },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Text("⚙")
        }

        if (uiState.mostrarConfig) {
            JanelaConfiguracoes(
                musicaMute = uiState.musicaMute,
                onAlternarMusica = { viewModel.alternarMusica() },
                onFechar = { viewModel.fecharConfig() }
            )
        }
    }
}
