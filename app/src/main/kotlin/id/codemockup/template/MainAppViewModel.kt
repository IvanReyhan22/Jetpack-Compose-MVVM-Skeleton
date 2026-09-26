package id.codemockup.template


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.codemockup.template.core.datastore.BaseDataStore
import id.codemockup.template.core.common.SessionManager
import id.codemockup.template.core.common.NetworkErrorManager
import id.codemockup.template.core.common.NetworkErrorType
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SessionAction {
    val id: Long
    data class ReturnToLogin(override val id: Long) : SessionAction
    data class ClearFailed(override val id: Long) : SessionAction
}

@HiltViewModel
class MainAppViewModel @Inject constructor(
    private val dataStore: BaseDataStore,
    private val sessionManager: SessionManager,
    private val networkErrorManager: NetworkErrorManager,
) : ViewModel() {
    private val _state = MutableStateFlow<MainAppState>(MainAppState.Loading)
    val state = _state.asStateFlow()
    private var loadJob: Job? = null

    private val _sessionAction = MutableStateFlow<SessionAction?>(null)
    val sessionAction = _sessionAction.asStateFlow()
    private val _retrying = MutableStateFlow(false)
    val retrying = _retrying.asStateFlow()
    val networkError = networkErrorManager.events

    init {
        loadSession()
        viewModelScope.launch {
            sessionManager.events.collectLatest { event ->
                if (event == null) {
                    _sessionAction.value = null
                    return@collectLatest
                }
                try {
                    val current = dataStore.session.first()
                    when {
                        current == null -> _sessionAction.value = SessionAction.ReturnToLogin(event.id)
                        current.token != event.expectedToken || !event.needsClear -> sessionManager.acknowledge(event.id)
                        else -> _sessionAction.value = SessionAction.ClearFailed(event.id)
                    }
                } catch (cancelled: CancellationException) {
                    throw cancelled
                } catch (_: Exception) {
                    _sessionAction.value = SessionAction.ClearFailed(event.id)
                }
            }
        }
    }

    fun acknowledgeNetworkError(type: NetworkErrorType) = networkErrorManager.acknowledge(type)
    fun acknowledgeSessionExpiry(id: Long) = sessionManager.acknowledge(id)

    fun retrySessionExpiry() {
        val event = sessionManager.events.value ?: return
        if (_retrying.value) return
        _retrying.value = true
        viewModelScope.launch {
            try {
                dataStore.clearSessionIfTokenMatches(event.expectedToken)
                if (dataStore.session.first() == null) {
                    // Also handles an earlier read failure after a successful clear.
                    _sessionAction.value = SessionAction.ReturnToLogin(event.id)
                } else {
                    sessionManager.acknowledge(event.id)
                }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                _sessionAction.value = SessionAction.ClearFailed(event.id)
            } finally {
                _retrying.value = false
            }
        }
    }

    fun loadSession() {
        if (loadJob?.isActive == true) return
        _state.value = MainAppState.Loading
        loadJob = viewModelScope.launch {
            _state.value = try {
                MainAppState.Ready(signedIn = dataStore.session.first() != null)
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                MainAppState.Error
            }
        }
    }
}
