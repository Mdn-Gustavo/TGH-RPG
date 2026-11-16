package com.example.ght.data

data class Inimigo(
    val nome: String,
    val vida: Int,
    val danoMin: Int,
    val danoMax: Int
)

data class Batalha(
    val chance: Int,
    val inimigo: Inimigo
)

data class Ataque(
    val nome: String,
    val danoMin: Int,
    val danoMax: Int,
    val chanceFalha: Int
)

object Ataques {
    private val porClasse = mapOf(
        "A" to listOf(
            Ataque("Golpe rápido", 2, 4, 5),
            Ataque("Golpe firme", 4, 8, 20),
            Ataque("Golpe pesado", 8, 16, 40)
        ),
        "B" to listOf(
            Ataque("Tiro simples", 1, 3, 0),
            Ataque("Tiro duplo", 3, 7, 15),
            Ataque("Tiro certeiro", 7, 14, 35)
        ),
        "C" to listOf(
            Ataque("Feitiço fraco", 2, 5, 10),
            Ataque("Feitiço médio", 5, 10, 25),
            Ataque("Feitiço forte", 10, 20, 50)
        ),
        "D" to listOf(
            Ataque("Corte curto", 3, 4, 5),
            Ataque("Corte duplo", 5, 9, 20),
            Ataque("Corte mortal", 9, 15, 35)
        )
    )

    fun da(classe: String): List<Ataque> = porClasse[classe] ?: porClasse.getValue("A")
}
