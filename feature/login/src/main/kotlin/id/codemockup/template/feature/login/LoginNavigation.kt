package id.codemockup.template.feature.login


import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import id.codemockup.template.core.data.remote.routes.Login
import id.codemockup.template.core.extensions.navigateAndClearBackStack

fun NavController.navigateToLogin() = navigateAndClearBackStack(Login)

fun NavGraphBuilder.loginScreen(navigateToMain: () -> Unit) {
    composable<Login> { LoginScreen(navigateToMain) }
}
