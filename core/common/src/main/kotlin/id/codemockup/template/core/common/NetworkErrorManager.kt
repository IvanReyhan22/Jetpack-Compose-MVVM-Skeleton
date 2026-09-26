package id.codemockup.template.core.common


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

enum class NetworkErrorType(val message: String) {
    TIMEOUT(ErrorMessageConstant.REQUEST_TIMEOUT),
    NO_CONNECTION(ErrorMessageConstant.NO_CONNECTION),
    SERVER_UNREACHABLE(ErrorMessageConstant.SERVER_UNREACHABLE),
}

/** A pending dialog survives backgrounding and is acknowledged on dismissal. */
@Singleton
class NetworkErrorManager @Inject constructor() {
    private val pending = MutableStateFlow<NetworkErrorType?>(null)
    val events = pending.asStateFlow()

    fun onNetworkError(type: NetworkErrorType) = pending.compareAndSet(null, type)
    fun acknowledge(type: NetworkErrorType) { pending.compareAndSet(type, null) }
}
