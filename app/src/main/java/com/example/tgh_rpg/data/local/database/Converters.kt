package com.example.tgh_rpg.data.local

import androidx.room.TypeConverter
import com.example.tgh_rpg.data.local.entity.StatusPersonagem
import com.example.tgh_rpg.data.local.entity.TipoEvento
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

// Converts values Room cannot store directly into supported SQLite values and back.
class Converters {
    // Store event types by enum name so the database does not depend on enum ordinal positions.
    @TypeConverter
    fun deTipoEventoParaString(tipo: TipoEvento): String = tipo.name

    // Restore the event type from the name written to the database.
    @TypeConverter
    fun deStringParaTipoEvento(valor: String): TipoEvento = TipoEvento.valueOf(valor)

    // Store character status as a stable, readable string.
    @TypeConverter
    fun deStatusParaString(status: StatusPersonagem): String = status.name

    // Restore character status from its stored enum name.
    @TypeConverter
    fun deStringParaStatus(valor: String): StatusPersonagem = StatusPersonagem.valueOf(valor)

    // Encode string lists as JSON because SQLite has no native list column type.
    @TypeConverter
    fun deListaParaString(lista: List<String>): String =
        Json.encodeToString(ListSerializer(String.serializer()), lista)

    // Decode the JSON text back into the string list used by the entity.
    @TypeConverter
    fun deStringParaLista(valor: String): List<String> =
        Json.decodeFromString(ListSerializer(String.serializer()), valor)
}