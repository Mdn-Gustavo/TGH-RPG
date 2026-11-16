package com.example.ght.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun JanelaConfiguracoes(
    musicaMute: Boolean,
    onAlternarMusica: () -> Unit,
    onFechar: () -> Unit
) {
    Dialog(onDismissRequest = onFechar) {
        Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Configurações")
                Spacer(modifier = Modifier.height(10.dp))
                Text("Mutar Música")
                Switch(
                    checked = musicaMute,
                    onCheckedChange = { onAlternarMusica() }
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(onClick = onFechar) {
                    Text("Fechar")
                }
            }
        }
    }
}
