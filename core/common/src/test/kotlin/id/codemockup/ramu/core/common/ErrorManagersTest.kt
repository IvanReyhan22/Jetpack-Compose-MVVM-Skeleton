package id.codemockup.ramu.core.common

import id.codemockup.ramu.core.common.utils.sentry.SentryLogger
import id.codemockup.ramu.core.common.utils.sentry.SentryService
import kotlinx.coroutines.CancellationException
import org.junit.Assert.*
import org.junit.Test
import java.net.UnknownHostException
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class ErrorManagersTest {
    @Test fun networkErrorsRemainPendingAndDeduplicateUntilDismissed() {
        val manager = NetworkErrorManager()
        assertTrue(manager.onNetworkError(NetworkErrorType.TIMEOUT))
        assertFalse(manager.onNetworkError(NetworkErrorType.NO_CONNECTION))
        assertEquals(NetworkErrorType.TIMEOUT, manager.events.value)
        manager.acknowledge(NetworkErrorType.NO_CONNECTION)
        assertNotNull(manager.events.value)
        manager.acknowledge(NetworkErrorType.TIMEOUT)
        assertNull(manager.events.value)
        assertTrue(manager.onNetworkError(NetworkErrorType.NO_CONNECTION))
    }

    @Test fun concurrentExpiryEmitsOnePendingEventAndSuccessOverridesStorageFailure() {
        val manager = SessionManager()
        val pool = Executors.newFixedThreadPool(4)
        repeat(20) { pool.submit { manager.onSessionExpired("token", needsClear = true) } }
        pool.shutdown()
        assertTrue(pool.awaitTermination(5, TimeUnit.SECONDS))
        val pending = manager.events.value!!
        manager.onSessionExpired("token")
        assertEquals(pending.id, manager.events.value!!.id)
        assertFalse(manager.events.value!!.needsClear)
        manager.onSessionExpired("token", needsClear = true)
        assertFalse(manager.events.value!!.needsClear)
        manager.acknowledge(pending.id + 1)
        assertNotNull(manager.events.value)
        manager.acknowledge(pending.id)
        assertNull(manager.events.value)
    }

    @Test fun disabledSentryHelpersAreNoOpsAndExpectedCausesAreRecognized() {
        assertFalse(SentryService.isEnabled)
        SentryLogger.captureException(IllegalStateException("sample"))
        SentryLogger.apiBreadcrumb("https://example.com", "GET", 500)
        assertTrue(SentryService.isExpectedFailure(RuntimeException(UnknownHostException())))
        assertTrue(SentryService.isExpectedFailure(CancellationException()))
        assertFalse(SentryService.isExpectedFailure(IllegalStateException("unexpected")))
    }
}
