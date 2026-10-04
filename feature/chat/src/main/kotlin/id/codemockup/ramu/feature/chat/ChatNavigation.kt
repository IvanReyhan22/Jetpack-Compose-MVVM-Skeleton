package id.codemockup.ramu.feature.chat

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import id.codemockup.ramu.core.data.remote.routes.Chat

fun NavGraphBuilder.chatScreen(onBack: (() -> Unit)? = null) {
    composable<Chat> { ChatScreen(onBack = onBack) }
}
