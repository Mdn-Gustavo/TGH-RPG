package com.example.ght.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ght.CasteloApp
import com.example.ght.data.Inventario
import com.example.ght.logic.Classe
import com.example.ght.logic.novoPersonagem
import com.example.ght.ui.classes.TelaClasses
import com.example.ght.ui.components.TelaEmConstrucao
import com.example.ght.ui.inventario.TelaInventario
import com.example.ght.ui.inventario.VMInventario
import com.example.ght.ui.menu.TelaMenu
import com.example.ght.ui.menu.VMMenu
import com.example.ght.ui.principal.TelaPrincipal
import com.example.ght.ui.principal.VMPrincipal
import kotlinx.coroutines.launch

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val container = (context.applicationContext as CasteloApp).container
    val scope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = RotaMenu,
        modifier = modifier
    ) {
        composable<RotaMenu> {
            val vmMenu = viewModel { VMMenu(container.personagemDao) }
            val menuState by vmMenu.uiState.collectAsState()

            TelaMenu(
                viewModel = vmMenu,
                onIrParaJogo = {
                    if (menuState.jogoCriado) {
                        navController.navigate(RotaPrincipal(salaAtual = 0)) {
                            launchSingleTop = true
                        }
                    } else {
                        navController.navigate(RotaClasses) {
                            launchSingleTop = true
                        }
                    }
                },
                onIrParaModoADM = {
                    navController.navigate(RotaModoADM) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<RotaClasses> {
            TelaClasses(
                onEscolherClasse = { letraClasse ->
                    scope.launch {
                        container.personagemDao.encerrarAtivos()
                        val novo = novoPersonagem("Herói", Classe.daLetra(letraClasse))
                        container.personagemDao.inserir(novo)
                        Inventario.limpar()
                        navController.navigate(RotaPrincipal(salaAtual = 0)) {
                            launchSingleTop = true
                        }
                    }
                }
            )
        }

        composable<RotaPrincipal> {
            val vmPrincipal = viewModel {
                VMPrincipal(
                    repository = container.encontroRepository,
                    personagemDao = container.personagemDao
                )
            }

            TelaPrincipal(
                viewModel = vmPrincipal,
                onIrParaInventario = {
                    navController.navigate(RotaInventario) {
                        launchSingleTop = true
                    }
                },
                onMorrer = {
                    navController.popBackStack(RotaMenu, inclusive = false)
                }
            )
        }

        composable<RotaInventario> {
            val vmInventario = viewModel { VMInventario() }

            TelaInventario(
                viewModel = vmInventario,
                onVoltar = { navController.popBackStack() }
            )
        }

        composable<RotaModoADM> {
            TelaEmConstrucao(
                titulo = "Modo ADM",
                onVoltar = { navController.popBackStack() }
            )
        }
    }
}
