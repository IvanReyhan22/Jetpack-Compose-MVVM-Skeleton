package id.codemockup.ramu.feature.login


import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import id.codemockup.ramu.core.data.remote.routes.Login
import id.codemockup.ramu.core.extensions.navigateAndClearBackStack

fun NavController.navigateToLogin() = navigateAndClearBackStack(Login)

fun NavGraphBuilder.loginScreen(navigateToMain: () -> Unit) {
    composable<Login> { LoginScreen(navigateToMain) }
}
