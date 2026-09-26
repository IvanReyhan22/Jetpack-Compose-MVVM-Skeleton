package id.codemockup.template.feature.main.navigations


import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import id.codemockup.template.core.data.remote.routes.Main
import id.codemockup.template.core.extensions.navigateAndClearBackStack
import id.codemockup.template.feature.main.MainScreen

fun NavController.navigateToMain() = navigateAndClearBackStack(Main)

fun NavGraphBuilder.mainScreen(navigateToLogin: () -> Unit) {
    composable<Main> { MainScreen(navigateToLogin) }
}
