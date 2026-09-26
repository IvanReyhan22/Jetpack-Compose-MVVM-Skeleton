package id.codemockup.template

import id.codemockup.template.core.common.SessionManager
import id.codemockup.template.core.common.NetworkErrorManager
import id.codemockup.template.core.datastore.BaseDataStore
import id.codemockup.template.core.model.session.User
import id.codemockup.template.core.model.session.UserSession
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainAppViewModelTest {
    @get:Rule internal val main = MainDispatcherRule()

    @Test fun resolvesInitialDestinationFromSession() = runTest {
        val store = FakeDataStore()
        val loggedOut = MainAppViewModel(store, SessionManager(), NetworkErrorManager())
        assertEquals(MainAppState.Loading, loggedOut.state.value)
        advanceUntilIdle()
        assertEquals(MainAppState.Ready(false), loggedOut.state.value)
        store.saveSession(UserSession("token", User("id", "email")))
        val loggedIn = MainAppViewModel(store, SessionManager(), NetworkErrorManager())
        advanceUntilIdle()
        assertEquals(MainAppState.Ready(true), loggedIn.state.value)
    }

    @Test fun readFailureCanBeRetried() = runTest {
        var failRead = true
        val store = object : BaseDataStore {
            override val session: Flow<UserSession?> = flow {
                if (failRead) error("Unreadable")
                emit(null)
            }
            override suspend fun saveSession(session: UserSession) = Unit
            override suspend fun clearSession() = Unit
            override suspend fun clearSessionIfTokenMatches(expectedToken: String) = false
        }
        val vm = MainAppViewModel(store, SessionManager(), NetworkErrorManager())
        advanceUntilIdle()
        assertEquals(MainAppState.Error, vm.state.value)
        failRead = false
        vm.loadSession()
        advanceUntilIdle()
        assertEquals(MainAppState.Ready(false), vm.state.value)
    }

    @Test fun expirationRemainsPendingUntilNavigationAndStorageFailureCanRetry() = runTest {
        val store = FakeDataStore()
        store.saveSession(UserSession("token", User("id", "email")))
        val sessions = SessionManager()
        val vm = MainAppViewModel(store, sessions, NetworkErrorManager())
        advanceUntilIdle()
        sessions.onSessionExpired("token", needsClear = true)
        advanceUntilIdle()
        assertTrue(vm.sessionAction.value is SessionAction.ClearFailed)
        store.failWrite = true
        vm.retrySessionExpiry()
        advanceUntilIdle()
        assertNotNull(store.session.value)
        assertTrue(vm.sessionAction.value is SessionAction.ClearFailed)
        store.failWrite = false
        vm.retrySessionExpiry()
        advanceUntilIdle()
        assertNull(store.session.value)
        assertTrue(vm.sessionAction.value is SessionAction.ReturnToLogin)
        assertNotNull(sessions.events.value)
        vm.acknowledgeSessionExpiry(vm.sessionAction.value!!.id)
        advanceUntilIdle()
        assertNull(vm.sessionAction.value)
    }

    @Test fun delayedExpiryDoesNotNavigateAwayFromNewSession() = runTest {
        val store = FakeDataStore()
        store.saveSession(UserSession("new-token", User("id", "email")))
        val sessions = SessionManager()
        val vm = MainAppViewModel(store, sessions, NetworkErrorManager())
        sessions.onSessionExpired("old-token")
        advanceUntilIdle()
        assertNull(vm.sessionAction.value)
        assertEquals("new-token", store.session.value?.token)
        assertNull(sessions.events.value)
    }
}
