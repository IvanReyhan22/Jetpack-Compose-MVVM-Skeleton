package id.codemockup.template.core.network.services


import id.codemockup.template.core.data.remote.request.LoginRequest
import id.codemockup.template.core.data.remote.response.Response
import id.codemockup.template.core.data.remote.response.auth.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthServices {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}
