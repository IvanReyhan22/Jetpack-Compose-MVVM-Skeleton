package id.codemockup.template.feature.login


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.codemockup.template.core.common.DataState
import id.codemockup.template.core.common.UiState
import id.codemockup.template.core.common.validation.LoginValidation
import id.codemockup.template.core.datastore.BaseDataStore
import id.codemockup.template.core.domain.mapper.toSession
import id.codemockup.template.core.domain.usecase.auth.AuthUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val dataStore: BaseDataStore,
) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()
    private val effects = Channel<LoginEffect>(Channel.BUFFERED)
    val effect = effects.receiveAsFlow()
    private var loginJob: Job? = null

    fun onEmailChanged(email: String) {
        if (!_state.value.login.isLoading) _state.update {
            it.copy(email = email, emailError = null, login = DataState())
        }
    }

    fun onPasswordChanged(password: String) {
        if (!_state.value.login.isLoading) _state.update {
            it.copy(password = password, passwordError = null, login = DataState())
        }
    }

    fun login() {
        if (loginJob?.isActive == true || _state.value.login.data != null) return
        val email = _state.value.email.trim()
        val password = _state.value.password
        val emailError = LoginValidation.emailError(email)
        val passwordError = LoginValidation.passwordError(password)
        _state.update { it.copy(emailError = emailError, passwordError = passwordError) }
        if (emailError != null || passwordError != null) return
        _state.update { it.copy(login = DataState(isLoading = true)) }
        loginJob = viewModelScope.launch {
            authUseCase.loginUseCase(email, password).collect { result ->
                when (result) {
                    UiState.Loading -> _state.update { it.copy(login = DataState(isLoading = true)) }
                    is UiState.Error -> _state.update {
                        it.copy(login = DataState(errorMessage = result.message))
                    }
                    is UiState.Success -> {
                        try {
                            dataStore.saveSession(result.data.toSession())
                            _state.update { it.copy(password = "", login = DataState(data = result.data)) }
                            effects.send(LoginEffect.SignedIn)
                        } catch (cancelled: CancellationException) {
                            throw cancelled
                        } catch (_: Exception) {
                            _state.update {
                                it.copy(login = DataState(errorMessage = "Could not save session. Try again."))
                            }
                        }
                    }
                }
            }
        }
    }
}
