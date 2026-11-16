package com.example.tgh_rpg.data.remote

import com.example.tgh_rpg.data.local.entity.EventoEntity
import com.example.tgh_rpg.data.local.entity.PersonagemEntity
import com.example.tgh_rpg.data.local.entity.StatusPersonagem
import com.example.tgh_rpg.data.local.entity.TipoEvento
import com.google.firebase.firestore.DocumentSnapshot

// Convert a Room character entity into Firestore's supported field/value map.
internal fun PersonagemEntity.toFirestoreMap(): Map<String, Any> = mapOf(
    "id" to id,
    "nome" to nome,
    "hpMaximo" to hpMaximo,
    "hpAtual" to hpAtual,
    "forca" to forca,
    "agilidade" to agilidade,
    "mente" to mente,
    "salaAtual" to salaAtual,
    "eventosVistos" to eventosVistos,
    "marcas" to marcas,
    "status" to status.name,
    "criadoEm" to criadoEm
)

// Convert a Room event entity into Firestore fields, storing its enum as a string.
internal fun EventoEntity.toFirestoreMap(): Map<String, Any> = mapOf(
    "id" to id,
    "titulo" to titulo,
    "tipo" to tipo.name,
    "texto" to texto,
    "escolhaJson" to escolhaJson,
    "atualizadaEm" to atualizadaEm
)

// Rebuild a character from a Firestore document, validating required fields and defaulting optional ones.
internal fun DocumentSnapshot.toPersonagemEntity(): PersonagemEntity {
    val values = data ?: error("Documento de personagem sem dados: $id")
    return PersonagemEntity(
        id = (values["id"] as? Number)?.toInt() ?: id.toIntOrNull()
        ?: error("ID de personagem inválido: $id"),
        nome = values.requiredString("nome"),
        hpMaximo = values.intOrDefault("hpMaximo", 5),
        hpAtual = values.intOrDefault("hpAtual", 5),
        forca = values.intOrDefault("forca", 5),
        agilidade = values.intOrDefault("agilidade", 7),
        mente = values.intOrDefault("mente", 7),
        salaAtual = values.intOrDefault("salaAtual", 1),
        eventosVistos = values.stringListOrEmpty("eventosVistos"),
        marcas = values.stringListOrEmpty("marcas"),
        status = values["status"]?.let {
            StatusPersonagem.valueOf(it as? String ?: error("Status inválido"))
        } ?: StatusPersonagem.EM_ANDAMENTO,
        criadoEm = (values["criadoEm"] as? Number)?.toLong() ?: 0L
    )
}

// Rebuild an event from its Firestore fields; required fields or an invalid event type cause an error.
internal fun DocumentSnapshot.toEventoEntity(): EventoEntity {
    val values = data ?: error("Documento de evento sem dados: $id")
    return EventoEntity(
        id = (values["id"] as? Number)?.toInt() ?: id.toIntOrNull()
        ?: error("ID de evento inválido: $id"),
        titulo = values.requiredString("titulo"),
        tipo = TipoEvento.valueOf(values.requiredString("tipo")),
        texto = values.requiredString("texto"),
        escolhaJson = values.requiredString("escolhaJson"),
        atualizadaEm = (values["atualizadaEm"] as? Number)?.toLong() ?: 0L
    )
}

// Read a required string field and fail clearly if Firestore data is missing or has the wrong type.
private fun Map<String, Any>.requiredString(key: String): String =
    this[key] as? String ?: error("Campo '$key' ausente ou inválido")

// Use a fallback for optional integer fields that are missing or stored with a different type.
private fun Map<String, Any>.intOrDefault(key: String, default: Int): Int =
    (this[key] as? Number)?.toInt() ?: default

// Read a string list; absent or non-list fields become empty, while invalid list items are rejected.
private fun Map<String, Any>.stringListOrEmpty(key: String): List<String> =
    (this[key] as? List<*>)?.map {
        it as? String ?: error("Item inválido na lista '$key'")
    } ?: emptyList()
