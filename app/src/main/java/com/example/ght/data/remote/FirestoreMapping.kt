package com.example.ght.data.remote

import com.example.ght.data.local.Atributo
import com.example.ght.data.local.EncontroEntity
import com.example.ght.data.local.Opcao
import com.example.ght.data.local.PersonagemEntity
import com.example.ght.data.local.StatusJogo
import com.google.firebase.firestore.DocumentSnapshot

internal fun PersonagemEntity.toFirestoreMap(): Map<String, Any> = mapOf(
    "id" to id,
    "nome" to nome,
    "classe" to classe,
    "forca" to forca,
    "agilidade" to agilidade,
    "mente" to mente,
    "vida" to vida,
    "vidaMax" to vidaMax,
    "eventos" to eventos,
    "acertos" to acertos,
    "status" to status.name,
    "recompensa" to recompensa,
    "criadoEm" to criadoEm
)

internal fun DocumentSnapshot.toPersonagemEntity(): PersonagemEntity {
    val values = data ?: error("Documento de personagem sem dados: $id")
    return PersonagemEntity(
        id = (values["id"] as? Number)?.toLong() ?: id.toLongOrNull()
            ?: error("ID de personagem inválido: $id"),
        nome = values.requiredString("nome"),
        classe = (values["classe"] as? String) ?: "A",
        forca = values.intOrDefault("forca", 3),
        agilidade = values.intOrDefault("agilidade", 3),
        mente = values.intOrDefault("mente", 3),
        vida = values.intOrDefault("vida", 20),
        vidaMax = values.intOrDefault("vidaMax", 20),
        eventos = values.intOrDefault("eventos", 0),
        acertos = values.intOrDefault("acertos", 0),
        status = values["status"]?.let {
            runCatching { StatusJogo.valueOf(it as String) }.getOrDefault(StatusJogo.EM_ANDAMENTO)
        } ?: StatusJogo.EM_ANDAMENTO,
        recompensa = (values["recompensa"] as? String) ?: "",
        criadoEm = (values["criadoEm"] as? Number)?.toLong() ?: 0L
    )
}

internal fun Opcao.toFirestoreMap(): Map<String, Any?> = mapOf(
    "rotulo" to rotulo,
    "atributo" to atributo.name,
    "dificuldade" to dificuldade,
    "textoSucesso" to textoSucesso,
    "textoFalha" to textoFalha,
    "chanceBatalha" to chanceBatalha,
    "inimigoNome" to inimigoNome,
    "inimigoVida" to inimigoVida,
    "inimigoDanoMin" to inimigoDanoMin,
    "inimigoDanoMax" to inimigoDanoMax,
    "itemNome" to itemNome,
    "itemEfeito" to itemEfeito,
    "itemBonusForca" to itemBonusForca,
    "itemBonusAgilidade" to itemBonusAgilidade,
    "itemBonusMente" to itemBonusMente,
    "itemCuraVida" to itemCuraVida
)

internal fun EncontroEntity.toFirestoreMap(): Map<String, Any> = mapOf(
    "id" to id,
    "titulo" to titulo,
    "texto" to texto,
    "opcaoA" to opcaoA.toFirestoreMap(),
    "opcaoB" to opcaoB.toFirestoreMap(),
    "opcaoC" to opcaoC.toFirestoreMap(),
    "atualizadoEm" to atualizadoEm
)

@Suppress("UNCHECKED_CAST")
private fun Map<String, Any?>.toOpcao(): Opcao {
    val attrStr = (this["atributo"] as? String) ?: Atributo.FORCA.name
    val attr = runCatching { Atributo.valueOf(attrStr) }.getOrDefault(Atributo.FORCA)
    return Opcao(
        rotulo = (this["rotulo"] as? String) ?: "",
        atributo = attr,
        dificuldade = (this["dificuldade"] as? Number)?.toInt() ?: 10,
        textoSucesso = (this["textoSucesso"] as? String) ?: "",
        textoFalha = (this["textoFalha"] as? String) ?: "",
        chanceBatalha = (this["chanceBatalha"] as? Number)?.toInt(),
        inimigoNome = this["inimigoNome"] as? String,
        inimigoVida = (this["inimigoVida"] as? Number)?.toInt() ?: 15,
        inimigoDanoMin = (this["inimigoDanoMin"] as? Number)?.toInt() ?: 2,
        inimigoDanoMax = (this["inimigoDanoMax"] as? Number)?.toInt() ?: 5,
        itemNome = this["itemNome"] as? String,
        itemEfeito = this["itemEfeito"] as? String,
        itemBonusForca = (this["itemBonusForca"] as? Number)?.toInt() ?: 0,
        itemBonusAgilidade = (this["itemBonusAgilidade"] as? Number)?.toInt() ?: 0,
        itemBonusMente = (this["itemBonusMente"] as? Number)?.toInt() ?: 0,
        itemCuraVida = (this["itemCuraVida"] as? Number)?.toInt() ?: 0
    )
}

@Suppress("UNCHECKED_CAST")
internal fun DocumentSnapshot.toEncontroEntity(): EncontroEntity {
    val values = data ?: error("Documento de encontro sem dados: $id")
    val mapA = values["opcaoA"] as? Map<String, Any?> ?: emptyMap()
    val mapB = values["opcaoB"] as? Map<String, Any?> ?: emptyMap()
    val mapC = values["opcaoC"] as? Map<String, Any?> ?: emptyMap()

    return EncontroEntity(
        id = (values["id"] as? String)?.takeIf { it.isNotBlank() } ?: id,
        titulo = values.requiredString("titulo"),
        texto = (values["texto"] as? String) ?: "",
        opcaoA = mapA.toOpcao(),
        opcaoB = mapB.toOpcao(),
        opcaoC = mapC.toOpcao(),
        atualizadoEm = (values["atualizadoEm"] as? Number)?.toLong() ?: 0L
    )
}

private fun Map<String, Any>.requiredString(key: String): String =
    this[key] as? String ?: error("Campo '$key' ausente ou inválido")

private fun Map<String, Any>.intOrDefault(key: String, default: Int): Int =
    (this[key] as? Number)?.toInt() ?: default
