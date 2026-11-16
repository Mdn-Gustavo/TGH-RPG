package com.example.ght.navigation

import kotlinx.serialization.Serializable

@Serializable
object RotaMenu

@Serializable
data class RotaPrincipal(
    val salaAtual: Int
)

@Serializable
object RotaModoADM

@Serializable
object RotaClasses

@Serializable
object RotaInventario
