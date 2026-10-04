package id.codemockup.ramu.core.network.demo


import id.codemockup.ramu.core.data.remote.request.LoginRequest
import id.codemockup.ramu.core.data.remote.response.Response
import id.codemockup.ramu.core.data.remote.response.auth.LoginResponse
import id.codemockup.ramu.core.network.services.AuthServices
import kotlinx.coroutines.delay
import javax.inject.Inject

class DemoAuthServices @Inject constructor() : AuthServices {
    override suspend fun login(request: LoginRequest): Response<LoginResponse> {
        delay(600)
        if (request.email != "demo@example.com" || request.password != "password123") {
            throw IllegalArgumentException("Invalid email or password.")
        }
        return Response(LoginResponse("demo-session-token", "demo-user", request.email))
    }
}
