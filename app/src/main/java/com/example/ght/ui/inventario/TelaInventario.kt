package com.example.ght.ui.inventario

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ght.ui.theme.FonteTerminal
import com.example.ght.ui.theme.LaranjaTerminal

@Composable
fun TelaInventario(
    viewModel: VMInventario,
    onVoltar: () -> Unit
) {
    val itens by viewModel.itens.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 64.dp, bottom = 8.dp)
        ) {
            if (itens.isEmpty()) {
                item {
                    Text(
                        text = "> Inventário vazio",
                        color = LaranjaTerminal,
                        fontFamily = FonteTerminal,
                        fontSize = 16.sp
                    )
                }
            }

            items(itens) { item ->
                Text(
                    text = "> ${item.nome} ${item.efeito}",
                    color = LaranjaTerminal,
                    fontFamily = FonteTerminal,
                    fontSize = 16.sp
                )
            }
        }

        Button(
            onClick = onVoltar,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        ) {
            Text("Voltar")
        }
    }
}
