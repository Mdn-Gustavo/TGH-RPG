package com.example.tgh_rpg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tgh_rpg.data.local.entity.EventoEntity
import com.example.tgh_rpg.data.local.entity.PersonagemEntity
import com.example.tgh_rpg.ui.GameUiState
import com.example.tgh_rpg.ui.GameViewModel
import com.example.tgh_rpg.ui.theme.TGHRPGTheme

// Hosts the Compose UI and obtains a lifecycle-aware GameViewModel.
class MainActivity : ComponentActivity() {
    // The factory gives the ViewModel the Room DAOs and Firebase-backed repositories.
    private val gameViewModel: GameViewModel by viewModels {
        GameViewModel.Factory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TGHRPGTheme {
                // Collecting StateFlow makes Compose redraw when Room-backed UI state changes.
                val uiState by gameViewModel.uiState.collectAsStateWithLifecycle()
                GameScreen(
                    uiState = uiState,
                    onCreateCharacter = gameViewModel::criarPersonagem,
                    onUpdateCharacter = gameViewModel::atualizarPersonagem,
                    onDeleteCharacter = gameViewModel::excluirPersonagem,
                    onCreateEvent = gameViewModel::criarEvento,
                    onUpdateEvent = gameViewModel::atualizarEvento,
                    onDeleteEvent = gameViewModel::excluirEvento
                )
            }
        }
    }
}

// Renders the game lists and forwards user actions to the ViewModel as callbacks.
@Composable
private fun GameScreen(
    uiState: GameUiState,
    onCreateCharacter: (String) -> Unit,
    onUpdateCharacter: (PersonagemEntity, Int) -> Unit,
    onDeleteCharacter: (PersonagemEntity) -> Unit,
    onCreateEvent: (String) -> Unit,
    onUpdateEvent: (EventoEntity) -> Unit,
    onDeleteEvent: (EventoEntity) -> Unit
) {
    // Save partially entered form values across Activity recreation.
    var characterName by rememberSaveable { mutableStateOf("") }
    var eventTitle by rememberSaveable { mutableStateOf("") }

    Scaffold { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("TGH-RPG - dados locais")
            // Show repository or listener failures instead of hiding database errors.
            uiState.erroBanco?.let { Text("Erro do banco: $it") }
            Text("Personagens")
            OutlinedTextField(
                value = characterName,
                onValueChange = { characterName = it },
                label = { Text("Nome do personagem") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            // Create only when a name is present, then clear the input after submitting.
            Button(
                onClick = {
                    onCreateCharacter(characterName)
                    characterName = ""
                },
                enabled = characterName.isNotBlank()
            ) {
                Text("Criar personagem")
            }
            if (uiState.personagens.isEmpty()) {
                Text("Nenhum personagem salvo.")
            }
            // Render one card per character from the current reactive UI state.
            uiState.personagens.forEach { personagem ->
                CharacterCard(
                    personagem = personagem,
                    onUpdate = { variation -> onUpdateCharacter(personagem, variation) },
                    onDelete = { onDeleteCharacter(personagem) }
                )
            }

            Text("Eventos")
            OutlinedTextField(
                value = eventTitle,
                onValueChange = { eventTitle = it },
                label = { Text("Título do evento") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            // Create only when a title is present, then clear the event form.
            Button(
                onClick = {
                    onCreateEvent(eventTitle)
                    eventTitle = ""
                },
                enabled = eventTitle.isNotBlank()
            ) {
                Text("Criar evento")
            }
            if (uiState.eventos.isEmpty()) {
                Text("Nenhum evento salvo.")
            }
            // Render each event and connect its edit/delete buttons to the callbacks.
            uiState.eventos.forEach { evento ->
                EventCard(
                    evento = evento,
                    onUpdate = { onUpdateEvent(evento) },
                    onDelete = { onDeleteEvent(evento) }
                )
            }

        }
    }
}

// Shows one character and exposes HP adjustments and deletion as callbacks.
@Composable
private fun CharacterCard(
    personagem: PersonagemEntity,
    onUpdate: (Int) -> Unit,
    onDelete: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(personagem.nome)
            Text("HP: ${personagem.hpAtual}/${personagem.hpMaximo}")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { onUpdate(-1) }) { Text("-HP") }
                OutlinedButton(onClick = { onUpdate(1) }) { Text("+HP") }
                Button(onClick = onDelete) { Text("Excluir") }
            }
        }
    }
}

// Shows event details and delegates edits and deletion to the parent screen.
@Composable
private fun EventCard(
    evento: EventoEntity,
    onUpdate: () -> Unit,
    onDelete: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(evento.titulo)
            Text(evento.tipo.name)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onUpdate) { Text("Editar") }
                Button(onClick = onDelete) { Text("Excluir") }
            }
        }
    }
}
