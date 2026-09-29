package id.codemockup.ramu.navigation


import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import id.codemockup.ramu.core.common.ErrorMessageConstant
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import id.codemockup.ramu.core.data.remote.routes.Login
import id.codemockup.ramu.core.data.remote.routes.Main
import id.codemockup.ramu.feature.login.loginScreen
import id.codemockup.ramu.feature.login.navigateToLogin
import id.codemockup.ramu.feature.main.navigations.mainScreen
import id.codemockup.ramu.feature.main.navigations.navigateToMain

@Composable
fun AppNavHost(
    signedIn: Boolean,
    sessionExpiryId: Long? = null,
    onSessionExpiryHandled: (Long) -> Unit = {},
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    NavHost(navController, startDestination = if (signedIn) Main else Login) {
        loginScreen(navigateToMain = { navController.navigateToMain() })
        mainScreen(navigateToLogin = { navController.navigateToLogin() })
    }
    LaunchedEffect(sessionExpiryId) {
        sessionExpiryId?.let {
            navController.navigateToLogin()
            Toast.makeText(context, ErrorMessageConstant.SESSION_EXPIRED, Toast.LENGTH_LONG).show()
            onSessionExpiryHandled(it)
        }
    }
}
