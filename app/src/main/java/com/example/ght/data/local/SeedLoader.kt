package com.example.ght.data.local

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

class SeedLoader(private val context: Context, private val dao: EncontroDao) {

    suspend fun carregarSeVazio() {
        if (dao.contar() > 0) return
        val texto = context.assets.open("seed_encontros.json")
            .bufferedReader().use { it.readText() }
        dao.salvarTodos(lerEncontros(texto))
    }

    private fun lerEncontros(texto: String): List<EncontroEntity> {
        val lista = JSONArray(texto)
        return List(lista.length()) { i ->
            val o = lista.getJSONObject(i)
            EncontroEntity(
                id = o.getString("id"),
                titulo = o.getString("titulo"),
                texto = o.getString("texto"),
                opcaoA = lerOpcao(o.getJSONObject("opcaoA")),
                opcaoB = lerOpcao(o.getJSONObject("opcaoB")),
                opcaoC = if (o.has("opcaoC")) lerOpcao(o.getJSONObject("opcaoC")) else Opcao()
            )
        }
    }

    private fun lerOpcao(o: JSONObject) = Opcao(
        rotulo = o.getString("rotulo"),
        atributo = Atributo.valueOf(o.getString("atributo")),
        dificuldade = o.optInt("dificuldade", 10),
        textoSucesso = o.optString("textoSucesso", ""),
        textoFalha = o.optString("textoFalha", ""),
        chanceBatalha = if (o.has("chanceBatalha") && !o.isNull("chanceBatalha")) o.getInt("chanceBatalha") else null,
        inimigoNome = if (o.has("inimigoNome") && !o.isNull("inimigoNome")) o.getString("inimigoNome") else null,
        inimigoVida = o.optInt("inimigoVida", 15),
        inimigoDanoMin = o.optInt("inimigoDanoMin", 2),
        inimigoDanoMax = o.optInt("inimigoDanoMax", 5),
        itemNome = if (o.has("itemNome") && !o.isNull("itemNome")) o.getString("itemNome") else null,
        itemEfeito = if (o.has("itemEfeito") && !o.isNull("itemEfeito")) o.getString("itemEfeito") else null,
        itemBonusForca = o.optInt("itemBonusForca", 0),
        itemBonusAgilidade = o.optInt("itemBonusAgilidade", 0),
        itemBonusMente = o.optInt("itemBonusMente", 0),
        itemCuraVida = o.optInt("itemCuraVida", 0)
    )
}
