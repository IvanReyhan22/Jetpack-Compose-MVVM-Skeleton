package id.codemockup.ramu.feature.login


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import id.codemockup.ramu.feature.login.components.LoginContent

@Composable
fun LoginScreen(navigateToMain: () -> Unit, viewModel: LoginViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val onSignedIn by rememberUpdatedState(navigateToMain)
    LaunchedEffect(viewModel, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effect.collect { effect ->
                when (effect) { LoginEffect.SignedIn -> onSignedIn() }
            }
        }
    }
    LoginContent(state, viewModel::onEmailChanged, viewModel::onPasswordChanged, viewModel::login)
}
