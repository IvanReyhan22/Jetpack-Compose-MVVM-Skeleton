package id.codemockup.ramu.feature.main

import id.codemockup.ramu.core.model.session.User
import id.codemockup.ramu.core.model.session.UserSession
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    @get:Rule internal val main = MainDispatcherRule()

    @Test fun logoutClearsSessionBeforeNavigation() = runTest {
        val store = FakeDataStore()
        store.saveSession(UserSession("token", User("id", "demo@example.com")))
        val vm = MainViewModel(store)
        advanceUntilIdle()
        assertEquals("demo@example.com", vm.state.value.email)
        val effect = async { vm.effect.first().also { assertNull(store.session.value) } }
        vm.logout()
        advanceUntilIdle()
        assertEquals(MainEffect.SignedOut, effect.await())
    }

    @Test fun failedLogoutRetainsSessionAndAllowsRetry() = runTest {
        val store = FakeDataStore()
        store.saveSession(UserSession("token", User("id", "demo@example.com")))
        val vm = MainViewModel(store)
        advanceUntilIdle()
        store.failWrite = true
        vm.logout()
        advanceUntilIdle()
        assertNotNull(store.session.value)
        assertFalse(vm.state.value.isLoading)
        assertTrue(vm.state.value.error.isNotEmpty())
        store.failWrite = false
        vm.logout()
        advanceUntilIdle()
        assertNull(store.session.value)
    }
}
