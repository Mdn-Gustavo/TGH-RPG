package com.example.ght.ui.principal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ght.ui.components.JanelaConfiguracoes
import com.example.ght.ui.theme.FonteTerminal
import com.example.ght.ui.theme.VerdeTerminal
import kotlinx.coroutines.delay

@Composable
fun TelaPrincipal(
    viewModel: VMPrincipal,
    onIrParaInventario: () -> Unit,
    onMorrer: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val emBatalha = uiState.batalha != null
    val linhas = uiState.batalha?.linhas ?: uiState.linhas

    LaunchedEffect(uiState.morreu) {
        if (uiState.morreu) {
            delay(2500)
            onMorrer()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        // ======== PARTE DE CIMA (60%): terminal ========
        Box(
            modifier = Modifier
                .weight(6f)
                .fillMaxWidth()
                .background(Color.Black)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp, top = 64.dp, bottom = 8.dp),
                reverseLayout = true
            ) {
                items(linhas.asReversed()) { linha ->
                    Text(
                        text = linha,
                        color = VerdeTerminal,
                        fontFamily = FonteTerminal,
                        fontSize = 16.sp
                    )
                }
            }

            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (!emBatalha) {
                    Button(onClick = onIrParaInventario) {
                        Text("🎒")
                    }
                }
                Button(onClick = { viewModel.abrirConfig() }) {
                    Text("⚙")
                }
            }
        }

        // ======== PARTE DE BAIXO (40%) ========
        if (!emBatalha) {
            Row(
                modifier = Modifier
                    .weight(4f)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BotaoOpcao(letra = "A", onClick = { viewModel.escolherA() })
                BotaoOpcao(letra = "B", onClick = { viewModel.escolherB() })
            }
        } else {
            Column(
                modifier = Modifier
                    .weight(4f)
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    BotaoBatalha(texto = viewModel.ataques[0].nome, onClick = { viewModel.atacar(0) })
                    BotaoBatalha(texto = viewModel.ataques[1].nome, onClick = { viewModel.atacar(1) })
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    BotaoBatalha(texto = viewModel.ataques[2].nome, onClick = { viewModel.atacar(2) })
                    BotaoBatalha(texto = "Fugir", onClick = { viewModel.fugir() })
                }
            }
        }
    }

    if (uiState.mostrarConfig) {
        JanelaConfiguracoes(
            musicaMute = uiState.musicaMute,
            onAlternarMusica = { viewModel.alternarMusica() },
            onFechar = { viewModel.fecharConfig() }
        )
    }
}

@Composable
private fun BotaoOpcao(
    letra: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RectangleShape,
        modifier = Modifier.size(130.dp)
    ) {
        Text(text = letra, fontSize = 48.sp)
    }
}

@Composable
private fun RowScope.BotaoBatalha(
    texto: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RectangleShape,
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
    ) {
        Text(text = texto)
    }
}
