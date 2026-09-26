package id.codemockup.template.core.domain.repository.auth


import id.codemockup.template.core.data.remote.request.LoginRequest
import id.codemockup.template.core.data.remote.response.Response
import id.codemockup.template.core.data.remote.response.auth.LoginResponse

interface AuthRepository {
    suspend fun login(request: LoginRequest): Response<LoginResponse>
}
