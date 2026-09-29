package id.codemockup.ramu.core.domain.repository.auth


import id.codemockup.ramu.core.data.remote.request.LoginRequest
import id.codemockup.ramu.core.network.services.AuthServices
import javax.inject.Inject

class AuthDataSource @Inject constructor(private val services: AuthServices) : AuthRepository {
    override suspend fun login(request: LoginRequest) = services.login(request)
}
