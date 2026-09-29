package id.codemockup.ramu.core.domain.repository.auth


import id.codemockup.ramu.core.data.remote.request.LoginRequest
import id.codemockup.ramu.core.data.remote.response.Response
import id.codemockup.ramu.core.data.remote.response.auth.LoginResponse

interface AuthRepository {
    suspend fun login(request: LoginRequest): Response<LoginResponse>
}
