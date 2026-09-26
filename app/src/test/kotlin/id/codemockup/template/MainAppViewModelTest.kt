package id.codemockup.template

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
        val loggedOut = MainAppViewModel(store)
        assertEquals(MainAppState.Loading, loggedOut.state.value)
        advanceUntilIdle()
        assertEquals(MainAppState.Ready(false), loggedOut.state.value)
        store.saveSession(UserSession("token", User("id", "email")))
        val loggedIn = MainAppViewModel(store)
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
        }
        val vm = MainAppViewModel(store)
        advanceUntilIdle()
        assertEquals(MainAppState.Error, vm.state.value)
        failRead = false
        vm.loadSession()
        advanceUntilIdle()
        assertEquals(MainAppState.Ready(false), vm.state.value)
    }
}
