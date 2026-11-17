package com.example.ght.ui.modoadm

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.ght.data.local.Atributo

@Composable
fun TelaFormEncontro(
    viewModel: VMFormEncontro,
    onVoltar: () -> Unit
) {
    val estado by viewModel.uiState.collectAsState()

    LaunchedEffect(estado.salvo) {
        if (estado.salvo) onVoltar()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = if (estado.editando) "EDITAR ENCONTRO" else "NOVO ENCONTRO",
            style = MaterialTheme.typography.titleLarge
        )

        if (estado.carregando) {
            Text("Carregando...")
        } else {
            Text("Campos com * são obrigatórios.")

            Campo(
                rotulo = "Título",
                valor = estado.titulo,
                aoMudar = viewModel::mudarTitulo,
                obrigatorio = true,
                mostrarErros = estado.mostrarErros
            )
            Campo(
                rotulo = "Conteúdo do encontro",
                valor = estado.texto,
                aoMudar = viewModel::mudarTexto,
                obrigatorio = true,
                mostrarErros = estado.mostrarErros,
                minLinhas = 3
            )

            BlocoOpcao(
                letra = "A",
                opcao = estado.opcaoA,
                aoMudar = viewModel::mudarOpcaoA,
                mostrarErros = estado.mostrarErros
            )
            BlocoOpcao(
                letra = "B",
                opcao = estado.opcaoB,
                aoMudar = viewModel::mudarOpcaoB,
                mostrarErros = estado.mostrarErros
            )

            estado.erros.forEach { mensagem ->
                Text(text = mensagem, color = MaterialTheme.colorScheme.error)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onVoltar,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancelar")
                }
                Button(
                    onClick = { viewModel.salvar() },
                    enabled = !estado.salvando,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (estado.salvando) "Salvando..." else "Salvar")
                }
            }
        }
    }
}

@Composable
private fun BlocoOpcao(
    letra: String,
    opcao: OpcaoForm,
    aoMudar: (OpcaoForm) -> Unit,
    mostrarErros: Boolean
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "OPÇÃO $letra", style = MaterialTheme.typography.titleMedium)

            Campo(
                rotulo = "Texto da opção",
                valor = opcao.rotulo,
                aoMudar = { aoMudar(opcao.copy(rotulo = it)) },
                obrigatorio = true,
                mostrarErros = mostrarErros
            )

            Text("Atributo do teste")
            Atributo.entries.forEach { atributo ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { aoMudar(opcao.copy(atributo = atributo)) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = opcao.atributo == atributo,
                        onClick = { aoMudar(opcao.copy(atributo = atributo)) }
                    )
                    Text(nomeDoAtributo(atributo))
                }
            }

            Campo(
                rotulo = "Dificuldade (padrão 10)",
                valor = opcao.dificuldade,
                aoMudar = { aoMudar(opcao.copy(dificuldade = it)) },
                numerico = true
            )
            Campo(
                rotulo = "Resultado se der certo",
                valor = opcao.textoSucesso,
                aoMudar = { aoMudar(opcao.copy(textoSucesso = it)) },
                obrigatorio = true,
                mostrarErros = mostrarErros,
                minLinhas = 2
            )
            Campo(
                rotulo = "Resultado se der errado",
                valor = opcao.textoFalha,
                aoMudar = { aoMudar(opcao.copy(textoFalha = it)) },
                obrigatorio = true,
                mostrarErros = mostrarErros,
                minLinhas = 2
            )

            Text(text = "Batalha (opcional)", style = MaterialTheme.typography.titleSmall)
            Campo(
                rotulo = "Chance de batalha (0 a 100)",
                valor = opcao.chanceBatalha,
                aoMudar = { aoMudar(opcao.copy(chanceBatalha = it)) },
                numerico = true
            )
            Campo(
                rotulo = "Nome do inimigo",
                valor = opcao.inimigoNome,
                aoMudar = { aoMudar(opcao.copy(inimigoNome = it)) }
            )
            Campo(
                rotulo = "Vida do inimigo",
                valor = opcao.inimigoVida,
                aoMudar = { aoMudar(opcao.copy(inimigoVida = it)) },
                numerico = true
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Campo(
                    rotulo = "Dano mín.",
                    valor = opcao.inimigoDanoMin,
                    aoMudar = { aoMudar(opcao.copy(inimigoDanoMin = it)) },
                    numerico = true,
                    modifier = Modifier.weight(1f)
                )
                Campo(
                    rotulo = "Dano máx.",
                    valor = opcao.inimigoDanoMax,
                    aoMudar = { aoMudar(opcao.copy(inimigoDanoMax = it)) },
                    numerico = true,
                    modifier = Modifier.weight(1f)
                )
            }

            Text(text = "Item (opcional)", style = MaterialTheme.typography.titleSmall)
            Campo(
                rotulo = "Nome do item",
                valor = opcao.itemNome,
                aoMudar = { aoMudar(opcao.copy(itemNome = it)) }
            )
            Campo(
                rotulo = "Efeito (texto do inventário)",
                valor = opcao.itemEfeito,
                aoMudar = { aoMudar(opcao.copy(itemEfeito = it)) }
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Campo(
                    rotulo = "Bônus força",
                    valor = opcao.itemBonusForca,
                    aoMudar = { aoMudar(opcao.copy(itemBonusForca = it)) },
                    numerico = true,
                    modifier = Modifier.weight(1f)
                )
                Campo(
                    rotulo = "Bônus agilidade",
                    valor = opcao.itemBonusAgilidade,
                    aoMudar = { aoMudar(opcao.copy(itemBonusAgilidade = it)) },
                    numerico = true,
                    modifier = Modifier.weight(1f)
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Campo(
                    rotulo = "Bônus mente",
                    valor = opcao.itemBonusMente,
                    aoMudar = { aoMudar(opcao.copy(itemBonusMente = it)) },
                    numerico = true,
                    modifier = Modifier.weight(1f)
                )
                Campo(
                    rotulo = "Cura de vida",
                    valor = opcao.itemCuraVida,
                    aoMudar = { aoMudar(opcao.copy(itemCuraVida = it)) },
                    numerico = true,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun Campo(
    rotulo: String,
    valor: String,
    aoMudar: (String) -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    obrigatorio: Boolean = false,
    mostrarErros: Boolean = false,
    numerico: Boolean = false,
    minLinhas: Int = 1
) {
    OutlinedTextField(
        value = valor,
        onValueChange = aoMudar,
        label = { Text(if (obrigatorio) "$rotulo *" else rotulo) },
        isError = obrigatorio && mostrarErros && valor.isBlank(),
        keyboardOptions = if (numerico) {
            KeyboardOptions(keyboardType = KeyboardType.Number)
        } else {
            KeyboardOptions.Default
        },
        singleLine = minLinhas == 1,
        minLines = minLinhas,
        modifier = modifier
    )
}

private fun nomeDoAtributo(atributo: Atributo): String = when (atributo) {
    Atributo.FORCA -> "Força"
    Atributo.AGILIDADE -> "Agilidade"
    Atributo.MENTE -> "Mente"
}
