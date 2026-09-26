package id.codemockup.template


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.codemockup.template.core.datastore.BaseDataStore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainAppViewModel @Inject constructor(private val dataStore: BaseDataStore) : ViewModel() {
    private val _state = MutableStateFlow<MainAppState>(MainAppState.Loading)
    val state = _state.asStateFlow()
    private var loadJob: Job? = null

    init { loadSession() }

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
