package id.codemockup.ramu.feature.login.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.codemockup.ramu.designsystem.components.buttons.AppButton
import id.codemockup.ramu.designsystem.components.inputs.AppTextField
import id.codemockup.ramu.designsystem.theme.RamuTheme
import id.codemockup.ramu.feature.login.LoginState

@Composable
fun LoginContent(
    state: LoginState,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLogin: () -> Unit,
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    Column(
        Modifier.fillMaxSize().safeDrawingPadding().imePadding()
            .verticalScroll(rememberScrollState()).padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("Welcome", style = MaterialTheme.typography.headlineLarge)
        Text("Sign in to Ramu", style = MaterialTheme.typography.bodyLarge)
        AppTextField(
            value = state.email, onValueChange = onEmailChanged, label = "Email",
            modifier = Modifier.fillMaxWidth(), error = state.emailError,
            enabled = !state.login.isLoading,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        )
        AppTextField(
            value = state.password, onValueChange = onPasswordChanged, label = "Password",
            modifier = Modifier.fillMaxWidth(), error = state.passwordError,
            enabled = !state.login.isLoading,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                TextButton(onClick = { passwordVisible = !passwordVisible }) {
                    Text(if (passwordVisible) "Hide" else "Show")
                }
            },
        )
        if (state.login.errorMessage.isNotEmpty()) {
            Text(state.login.errorMessage, color = MaterialTheme.colorScheme.error)
        }
        AppButton("Sign in", onLogin, Modifier.fillMaxWidth(), loading = state.login.isLoading)
        Text("Demo account", style = MaterialTheme.typography.titleSmall)
        Text("demo@example.com / password123", style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginPreview() {
    RamuTheme { LoginContent(LoginState(), {}, {}, {}) }
}
