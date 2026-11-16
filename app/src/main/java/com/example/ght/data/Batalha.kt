package com.example.ght.data

// Um inimigo. Ex.: Inimigo("Orc", vida = 20, danoMin = 2, danoMax = 4)
data class Inimigo(
    val nome: String,
    val vida: Int,
    val danoMin: Int,
    val danoMax: Int
)

// "chance" é em porcentagem (0 a 100) de a batalha acontecer
data class Batalha(
    val chance: Int,
    val inimigo: Inimigo
)

// Um ataque do jogador. "chanceFalha" é em porcentagem (0 a 100)
data class Ataque(
    val nome: String,
    val danoMin: Int,
    val danoMax: Int,
    val chanceFalha: Int
)

// Ataques de cada classe (fixos, não mudam). Quanto mais forte, mais chance de falhar.
// Os números e nomes são de exemplo.
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
