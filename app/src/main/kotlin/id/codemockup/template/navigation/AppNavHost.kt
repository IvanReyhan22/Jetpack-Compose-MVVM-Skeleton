package id.codemockup.template.navigation


import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import id.codemockup.template.core.common.ErrorMessageConstant
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import id.codemockup.template.core.data.remote.routes.Login
import id.codemockup.template.core.data.remote.routes.Main
import id.codemockup.template.feature.login.loginScreen
import id.codemockup.template.feature.login.navigateToLogin
import id.codemockup.template.feature.main.navigations.mainScreen
import id.codemockup.template.feature.main.navigations.navigateToMain

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
