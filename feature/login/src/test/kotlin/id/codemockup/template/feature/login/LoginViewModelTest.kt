package id.codemockup.template.feature.login

import id.codemockup.template.core.data.remote.request.LoginRequest
import id.codemockup.template.core.data.remote.response.Response
import id.codemockup.template.core.data.remote.response.auth.LoginResponse
import id.codemockup.template.core.domain.repository.auth.AuthRepository
import id.codemockup.template.core.domain.usecase.auth.AuthUseCase
import id.codemockup.template.core.domain.usecase.auth.LoginUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    @get:Rule internal val main = MainDispatcherRule()
    private val store = FakeDataStore()
    private var calls = 0
    private var serviceFails = false
    private val repository = object : AuthRepository {
        override suspend fun login(request: LoginRequest): Response<LoginResponse> {
            calls++
            delay(600)
            if (serviceFails) error("Invalid credentials")
            return Response(LoginResponse("token", "id", request.email))
        }
    }
    private fun viewModel() = LoginViewModel(AuthUseCase(LoginUseCase(repository)), store)
    private fun LoginViewModel.fill() {
        onEmailChanged("  demo@example.com  ")
        onPasswordChanged("password123")
    }

    @Test fun rejectsInvalidFieldsWithoutCallingRepository() = runTest {
        val vm = viewModel()
        vm.login()
        assertNotNull(vm.state.value.emailError)
        assertNotNull(vm.state.value.passwordError)
        vm.onEmailChanged("invalid")
        vm.onPasswordChanged("password123")
        vm.login()
        advanceUntilIdle()
        assertNotNull(vm.state.value.emailError)
        assertEquals(0, calls)
    }

    @Test fun persistsBeforeNavigationAndPreventsDuplicateSubmissions() = runTest {
        val vm = viewModel()
        vm.fill()
        val effect = async { vm.effect.first().also { assertNotNull(store.session.value) } }
        vm.login()
        vm.login()
        assertTrue(vm.state.value.login.isLoading)
        advanceUntilIdle()
        assertEquals(1, calls)
        assertEquals(LoginEffect.SignedIn, effect.await())
        assertEquals("demo@example.com", store.session.value?.user?.email)
        assertEquals("", vm.state.value.password)
    }

    @Test fun serviceFailureCanBeRetried() = runTest {
        val vm = viewModel()
        vm.fill()
        serviceFails = true
        vm.login()
        advanceUntilIdle()
        assertEquals("Invalid credentials", vm.state.value.login.errorMessage)
        assertFalse(vm.state.value.login.isLoading)
        assertNull(store.session.value)
        serviceFails = false
        vm.login()
        advanceUntilIdle()
        assertNotNull(store.session.value)
    }

    @Test fun storageFailureDoesNotReportSuccessAndCanBeRetried() = runTest {
        val vm = viewModel()
        vm.fill()
        store.failWrite = true
        vm.login()
        advanceUntilIdle()
        assertNull(vm.state.value.login.data)
        assertNull(store.session.value)
        assertEquals("Could not save session. Try again.", vm.state.value.login.errorMessage)
        store.failWrite = false
        vm.login()
        advanceUntilIdle()
        assertNotNull(store.session.value)
    }
}
