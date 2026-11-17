package com.example.ght.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ght.AppContainer
import com.example.ght.ui.classes.VMClasses
import com.example.ght.ui.detalhe.VMDetalheEncontro
import com.example.ght.ui.inventario.VMInventario
import com.example.ght.ui.menu.VMMenu
import com.example.ght.ui.modoadm.VMFormEncontro
import com.example.ght.ui.modoadm.VMModoAdm
import com.example.ght.ui.principal.VMPrincipal

class AppViewModelFactory(
    private val container: AppContainer,
    private val encontroId: String? = null
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            VMMenu::class.java -> VMMenu(
                container.personagemRepository,
                container.audioManager
            )
            VMClasses::class.java -> VMClasses(container.personagemRepository)
            VMPrincipal::class.java -> VMPrincipal(
                repository = container.encontroRepository,
                personagemRepository = container.personagemRepository,
                inventarioRepository = container.inventarioRepository,
                audioManager = container.audioManager
            )
            VMInventario::class.java -> VMInventario(
                container.personagemRepository,
                container.inventarioRepository
            )
            VMModoAdm::class.java -> VMModoAdm(container.encontroRepository)
            VMFormEncontro::class.java -> VMFormEncontro(container.encontroRepository, encontroId)
            VMDetalheEncontro::class.java -> VMDetalheEncontro(container.encontroRepository, encontroId ?: "")
            else -> throw IllegalArgumentException("ViewModel desconhecido: ${modelClass.name}")
        }
        return viewModel as T
    }
}
