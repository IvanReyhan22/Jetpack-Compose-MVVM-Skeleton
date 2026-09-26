package id.codemockup.template


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import id.codemockup.template.core.common.ErrorMessageConstant
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import id.codemockup.template.core.designsystem.components.LoadingScreen
import id.codemockup.template.core.designsystem.components.TemplateButton
import id.codemockup.template.core.designsystem.theme.TemplateTheme
import id.codemockup.template.navigation.AppNavHost

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainAppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TemplateTheme {
                Surface(Modifier.fillMaxSize()) {
                    val state by viewModel.state.collectAsStateWithLifecycle()
                    val networkError by viewModel.networkError.collectAsStateWithLifecycle()
                    val sessionAction by viewModel.sessionAction.collectAsStateWithLifecycle()
                    val retrying by viewModel.retrying.collectAsStateWithLifecycle()
                    when (val current = state) {
                        MainAppState.Loading -> LoadingScreen()
                        MainAppState.Error -> Column(
                            Modifier.fillMaxSize().padding(24.dp),
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text("Could not load session.")
                            TemplateButton("Try again", viewModel::loadSession)
                        }
                        is MainAppState.Ready -> AppNavHost(
                            signedIn = current.signedIn,
                            sessionExpiryId = (sessionAction as? SessionAction.ReturnToLogin)?.id,
                            onSessionExpiryHandled = viewModel::acknowledgeSessionExpiry,
                        )
                    }
                    if (sessionAction is SessionAction.ClearFailed) {
                        AlertDialog(
                            onDismissRequest = {},
                            title = { Text("Session expired") },
                            text = { Text(ErrorMessageConstant.SESSION_STORAGE_ERROR) },
                            confirmButton = {
                                TextButton(onClick = viewModel::retrySessionExpiry, enabled = !retrying) {
                                    Text(if (retrying) "Retrying…" else "Try again")
                                }
                            },
                        )
                    } else networkError?.let { error ->
                        AlertDialog(
                            onDismissRequest = { viewModel.acknowledgeNetworkError(error) },
                            title = { Text("Connection problem") },
                            text = { Text(error.message) },
                            confirmButton = {
                                TextButton(onClick = { viewModel.acknowledgeNetworkError(error) }) { Text("OK") }
                            },
                        )
                    }
                }
            }
        }
    }
}
