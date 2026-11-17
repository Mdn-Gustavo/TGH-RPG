package com.example.ght.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TelaEmConstrucao(
    titulo: String,
    onVoltar: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = titulo)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Tela em construção")
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = onVoltar) {
            Text(text = "Voltar ao menu")
        }
    }
}
