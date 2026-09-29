package id.codemockup.ramu.core.common


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject
import javax.inject.Singleton

class SessionEvent(val id: Long, val expectedToken: String, val needsClear: Boolean)

@Singleton
class SessionManager @Inject constructor() {
    private val sequence = AtomicLong()
    private val pending = MutableStateFlow<SessionEvent?>(null)
    val events = pending.asStateFlow()

    fun onSessionExpired(expectedToken: String, needsClear: Boolean = false) {
        pending.update { current ->
            when {
                current?.expectedToken != expectedToken -> SessionEvent(sequence.incrementAndGet(), expectedToken, needsClear)
                !current.needsClear || needsClear -> current
                else -> SessionEvent(current.id, expectedToken, false)
            }
        }
    }

    fun acknowledge(id: Long) { pending.update { if (it?.id == id) null else it } }
    fun reset() { pending.value = null }
}
