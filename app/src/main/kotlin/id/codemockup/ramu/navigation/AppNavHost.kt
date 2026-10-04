package id.codemockup.ramu.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import id.codemockup.ramu.core.data.remote.routes.Chat
import id.codemockup.ramu.feature.chat.chatScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Chat) { chatScreen() }
}
