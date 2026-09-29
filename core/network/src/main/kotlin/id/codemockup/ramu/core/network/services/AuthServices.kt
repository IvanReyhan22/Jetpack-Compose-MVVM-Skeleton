package id.codemockup.ramu.core.network.services


import id.codemockup.ramu.core.data.remote.request.LoginRequest
import id.codemockup.ramu.core.data.remote.response.Response
import id.codemockup.ramu.core.data.remote.response.auth.LoginResponse
import retrofit2.http.Headers
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthServices {
    @Headers("X-Sensitive-Body: true")
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}
