package id.codemockup.template.core.network.demo


import id.codemockup.template.core.data.remote.request.LoginRequest
import id.codemockup.template.core.data.remote.response.Response
import id.codemockup.template.core.data.remote.response.auth.LoginResponse
import id.codemockup.template.core.network.services.AuthServices
import kotlinx.coroutines.delay
import javax.inject.Inject

/** Local sample only. Replace the DI binding when connecting a real backend. */
class DemoAuthServices @Inject constructor() : AuthServices {
    override suspend fun login(request: LoginRequest): Response<LoginResponse> {
        delay(600)
        if (request.email != "demo@example.com" || request.password != "password123") {
            throw IllegalArgumentException("Invalid email or password.")
        }
        return Response(LoginResponse("demo-session-token", "demo-user", request.email))
    }
}
