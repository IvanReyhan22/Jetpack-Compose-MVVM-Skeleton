package id.codemockup.ramu.feature.main.navigations


import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import id.codemockup.ramu.core.data.remote.routes.Main
import id.codemockup.ramu.core.extensions.navigateAndClearBackStack
import id.codemockup.ramu.feature.main.MainScreen

fun NavController.navigateToMain() = navigateAndClearBackStack(Main)

fun NavGraphBuilder.mainScreen(navigateToLogin: () -> Unit) {
    composable<Main> { MainScreen(navigateToLogin) }
}
