package id.codemockup.template.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import id.codemockup.template.core.data.remote.routes.Login
import id.codemockup.template.core.data.remote.routes.Main
import id.codemockup.template.feature.login.loginScreen
import id.codemockup.template.feature.login.navigateToLogin
import id.codemockup.template.feature.main.navigations.mainScreen
import id.codemockup.template.feature.main.navigations.navigateToMain

@Composable
fun AppNavHost(signedIn: Boolean) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = if (signedIn) Main else Login) {
        loginScreen(navigateToMain = { navController.navigateToMain() })
        mainScreen(navigateToLogin = { navController.navigateToLogin() })
    }
}
