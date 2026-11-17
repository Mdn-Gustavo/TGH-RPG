package com.example.ght.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.ght.CasteloApp
import com.example.ght.ui.AppViewModelFactory
import com.example.ght.ui.classes.TelaClasses
import com.example.ght.ui.classes.VMClasses
import com.example.ght.ui.detalhe.TelaDetalheEncontro
import com.example.ght.ui.detalhe.VMDetalheEncontro
import com.example.ght.ui.inventario.TelaInventario
import com.example.ght.ui.inventario.VMInventario
import com.example.ght.ui.menu.TelaMenu
import com.example.ght.ui.menu.VMMenu
import com.example.ght.ui.modoadm.TelaFormEncontro
import com.example.ght.ui.modoadm.TelaModoAdm
import com.example.ght.ui.modoadm.VMFormEncontro
import com.example.ght.ui.modoadm.VMModoAdm
import com.example.ght.ui.principal.TelaPrincipal
import com.example.ght.ui.principal.VMPrincipal

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val container = (context.applicationContext as CasteloApp).container

    NavHost(
        navController = navController,
        startDestination = RotaMenu,
        modifier = modifier
    ) {
        composable<RotaMenu> {
            val vmMenu = viewModel<VMMenu>(factory = AppViewModelFactory(container))
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
                onIrParaClasses = {
                    navController.navigate(RotaClasses) {
                        launchSingleTop = true
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
            val vmClasses = viewModel<VMClasses>(factory = AppViewModelFactory(container))

            TelaClasses(
                viewModel = vmClasses,
                onClasseCriada = {
                    navController.navigate(RotaPrincipal(salaAtual = 0)) {
                        popUpTo(RotaClasses) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<RotaPrincipal> {
            val vmPrincipal = viewModel<VMPrincipal>(factory = AppViewModelFactory(container))

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
            val vmInventario = viewModel<VMInventario>(factory = AppViewModelFactory(container))

            TelaInventario(
                viewModel = vmInventario,
                onVoltar = { navController.popBackStack() }
            )
        }

        composable<RotaModoADM> {
            val vmModoAdm = viewModel<VMModoAdm>(factory = AppViewModelFactory(container))

            TelaModoAdm(
                viewModel = vmModoAdm,
                onNovoEncontro = {
                    navController.navigate(RotaFormEncontro()) {
                        launchSingleTop = true
                    }
                },
                onEditarEncontro = { id ->
                    navController.navigate(RotaFormEncontro(encontroId = id)) {
                        launchSingleTop = true
                    }
                },
                onVerDetalhes = { id ->
                    navController.navigate(RotaDetalheEncontro(encontroId = id)) {
                        launchSingleTop = true
                    }
                },
                onVoltar = { navController.popBackStack() }
            )
        }

        composable<RotaFormEncontro> { backStackEntry ->
            val rota = backStackEntry.toRoute<RotaFormEncontro>()
            val vmForm = viewModel<VMFormEncontro>(factory = AppViewModelFactory(container, rota.encontroId))

            TelaFormEncontro(
                viewModel = vmForm,
                onVoltar = { navController.popBackStack() }
            )
        }

        composable<RotaDetalheEncontro> { backStackEntry ->
            val rota = backStackEntry.toRoute<RotaDetalheEncontro>()
            val vmDetalhe = viewModel<VMDetalheEncontro>(factory = AppViewModelFactory(container, rota.encontroId))

            TelaDetalheEncontro(
                viewModel = vmDetalhe,
                onVoltar = { navController.popBackStack() }
            )
        }
    }
}
