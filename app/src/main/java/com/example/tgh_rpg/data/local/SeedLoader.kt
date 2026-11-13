package com.example.tgh_rpg.data.local

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

class SeedLoader(private val context: Context, private val dao: EncontroDao) {

    // Só carrega se o banco estiver vazio; assim não apaga o que o admin editou
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
                opcaoC = lerOpcao(o.getJSONObject("opcaoC"))
            )
        }
    }

    private fun lerOpcao(o: JSONObject) = Opcao(
        rotulo = o.getString("rotulo"),
        atributo = Atributo.valueOf(o.getString("atributo")),
        dificuldade = o.optInt("dificuldade", 10),
        textoSucesso = o.optString("textoSucesso", ""),
        textoFalha = o.optString("textoFalha", "")
    )
}
