package com.example.tgh_rpg.data.local

import androidx.room.TypeConverter
import com.example.tgh_rpg.data.local.entity.TipoEvento

class Converters {

    @TypeConverter
    fun deTipoEventosParaString(tipo: TipoEvento): String{
        return tipo.name
    }
    @TypeConverter
    fun deStringParaTipoEvento(valor: String): TipoEvento {
        return TipoEvento.valueOf(valor)
    }
}