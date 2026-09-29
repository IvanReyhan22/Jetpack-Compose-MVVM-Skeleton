package id.codemockup.ramu.core.extensions


import androidx.navigation.NavController

inline fun <reified T : Any> NavController.navigateAndClearBackStack(route: T) {
    navigate(route) {
        popUpTo(graph.id) { inclusive = false }
        launchSingleTop = true
    }
}
