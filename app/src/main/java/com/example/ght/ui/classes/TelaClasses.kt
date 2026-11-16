package com.example.ght.ui.classes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TelaClasses(
    onEscolherClasse: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Text(text = "ESCOLHA SUA CLASSE")

        BotaoClasse(nome = "Classe A", onClick = { onEscolherClasse("A") })
        BotaoClasse(nome = "Classe B", onClick = { onEscolherClasse("B") })
        BotaoClasse(nome = "Classe C", onClick = { onEscolherClasse("C") })
        BotaoClasse(nome = "Classe D", onClick = { onEscolherClasse("D") })
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
