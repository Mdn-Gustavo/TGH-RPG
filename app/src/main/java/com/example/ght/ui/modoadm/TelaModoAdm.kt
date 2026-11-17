package com.example.ght.ui.modoadm

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.ght.data.local.EncontroEntity
import com.example.ght.data.local.dificuldadeMedia
import kotlinx.coroutines.launch

@Composable
fun TelaModoAdm(
    viewModel: VMModoAdm,
    onNovoEncontro: () -> Unit,
    onEditarEncontro: (String) -> Unit,
    onVerDetalhes: (String) -> Unit,
    onVoltar: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val escolherOrdem: (OrdemDificuldade) -> Unit = { ordem ->
        viewModel.mudarOrdem(ordem)
        scope.launch { drawerState.close() }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "FILTRO",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )
                Text(
                    text = "Ordenar por dificuldade",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                NavigationDrawerItem(
                    label = { Text("Padrão (por título)") },
                    selected = uiState.ordem == OrdemDificuldade.PADRAO,
                    onClick = { escolherOrdem(OrdemDificuldade.PADRAO) },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                NavigationDrawerItem(
                    label = { Text("Menor para maior") },
                    selected = uiState.ordem == OrdemDificuldade.MENOR_PRIMEIRO,
                    onClick = { escolherOrdem(OrdemDificuldade.MENOR_PRIMEIRO) },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                NavigationDrawerItem(
                    label = { Text("Maior para menor") },
                    selected = uiState.ordem == OrdemDificuldade.MAIOR_PRIMEIRO,
                    onClick = { escolherOrdem(OrdemDificuldade.MAIOR_PRIMEIRO) },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "MODO ADM", style = MaterialTheme.typography.titleLarge)
                Button(onClick = onVoltar) {
                    Text("Voltar")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onNovoEncontro,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("+ Novo encontro")
                }
                OutlinedButton(onClick = { scope.launch { drawerState.open() } }) {
                    Text("Filtro")
                }
            }

            uiState.erro?.let { mensagem ->
                Text(text = mensagem, color = MaterialTheme.colorScheme.error)
            }

            if (uiState.encontros.isEmpty()) {
                Text("Nenhum encontro criado ainda. Os encontros nativos do jogo não aparecem aqui e não podem ser alterados.")
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.encontros, key = { it.id }) { encontro ->
                        CartaoEncontro(
                            encontro = encontro,
                            onDetalhes = { onVerDetalhes(encontro.id) },
                            onEditar = { onEditarEncontro(encontro.id) },
                            onApagar = { viewModel.pedirParaApagar(encontro) }
                        )
                    }
                }
            }
        }
    }

    uiState.encontroParaApagar?.let { encontro ->
        AlertDialog(
            onDismissRequest = { viewModel.cancelarApagar() },
            title = { Text("Apagar encontro?") },
            text = { Text("\"${encontro.titulo}\" será removido do jogo.") },
            confirmButton = {
                Button(onClick = { viewModel.confirmarApagar() }) {
                    Text("Apagar")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.cancelarApagar() }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun CartaoEncontro(
    encontro: EncontroEntity,
    onDetalhes: () -> Unit,
    onEditar: () -> Unit,
    onApagar: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = encontro.titulo, style = MaterialTheme.typography.titleMedium)
            Text(text = encontro.texto, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Text(text = "Dificuldade: ${"%.1f".format(encontro.dificuldadeMedia)}")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onDetalhes) {
                    Text("Detalhes")
                }
                Button(onClick = onEditar) {
                    Text("Editar")
                }
                OutlinedButton(onClick = onApagar) {
                    Text("Apagar")
                }
            }
        }
    }
}
