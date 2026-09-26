package id.codemockup.template.core.domain

import id.codemockup.template.core.common.UiState
import id.codemockup.template.core.data.remote.request.LoginRequest
import id.codemockup.template.core.data.remote.response.Response
import id.codemockup.template.core.data.remote.response.auth.LoginResponse
import id.codemockup.template.core.domain.mapper.toSession
import id.codemockup.template.core.domain.repository.auth.AuthDataSource
import id.codemockup.template.core.domain.usecase.auth.LoginUseCase
import id.codemockup.template.core.network.services.AuthServices
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class LoginUseCaseTest {
    private val response = LoginResponse("token", "id", "demo@example.com")

    private fun useCase(block: suspend (LoginRequest) -> Response<LoginResponse>): LoginUseCase =
        LoginUseCase(AuthDataSource(object : AuthServices {
            override suspend fun login(request: LoginRequest) = block(request)
        }))

    @Test fun delegatesRequestAndEmitsLoadingThenSuccess() = runTest {
        var request: LoginRequest? = null
        val states = useCase { request = it; Response(response) }("demo@example.com", "secret").toList()
        assertEquals(LoginRequest("demo@example.com", "secret"), request)
        assertEquals(listOf(UiState.Loading, UiState.Success(response)), states)
        assertEquals("id", response.toSession().user.id)
        assertEquals("token", response.toSession().token)
    }

    @Test fun emitsLoadingThenError() = runTest {
        val states = useCase { throw IllegalStateException("Offline") }("email", "secret").toList()
        assertEquals(listOf(UiState.Loading, UiState.Error("Offline")), states)
    }

    @Test fun propagatesCancellationInsteadOfError() = runTest {
        val states = mutableListOf<UiState<LoginResponse>>()
        try {
            useCase { throw CancellationException("cancel") }("email", "secret").toList(states)
            fail("Expected cancellation")
        } catch (_: CancellationException) {
            assertEquals(listOf(UiState.Loading), states)
        }
    }
}
