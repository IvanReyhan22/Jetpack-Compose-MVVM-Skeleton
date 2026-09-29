package id.codemockup.ramu


import android.os.Bundle
import androidx.activity.SystemBarStyle
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
import id.codemockup.ramu.core.common.ErrorMessageConstant
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import id.codemockup.ramu.designsystem.components.feedback.LoadingScreen
import id.codemockup.ramu.designsystem.components.buttons.AppButton
import id.codemockup.ramu.designsystem.theme.RamuTheme
import id.codemockup.ramu.navigation.AppNavHost

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainAppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT),
        )
        setContent {
            RamuTheme {
                Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
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
                            AppButton("Try again", viewModel::loadSession)
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
