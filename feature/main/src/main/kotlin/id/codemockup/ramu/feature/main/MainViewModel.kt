package id.codemockup.ramu.feature.main


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.codemockup.ramu.core.datastore.BaseDataStore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val dataStore: BaseDataStore) : ViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()
    private val effects = Channel<MainEffect>(Channel.BUFFERED)
    val effect = effects.receiveAsFlow()
    private var logoutJob: Job? = null

    init {
        viewModelScope.launch {
            try {
                val session = dataStore.session.first()
                if (session == null) effects.send(MainEffect.SignedOut)
                _state.value = MainState(email = session?.user?.email.orEmpty(), isLoading = false)
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                _state.value = MainState(isLoading = false, error = "Could not read session. Sign out and try again.")
            }
        }
    }

    fun logout() {
        if (_state.value.isLoading || logoutJob?.isActive == true) return
        _state.update { it.copy(isLoading = true, error = "") }
        logoutJob = viewModelScope.launch {
            try {
                dataStore.clearSession()
                effects.send(MainEffect.SignedOut)
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                _state.update { it.copy(isLoading = false, error = "Could not clear session. Try again.") }
            }
        }
    }
}
