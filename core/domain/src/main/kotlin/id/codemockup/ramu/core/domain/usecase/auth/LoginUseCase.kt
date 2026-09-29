package id.codemockup.ramu.core.domain.usecase.auth


import id.codemockup.ramu.core.common.UiState
import id.codemockup.ramu.core.data.remote.request.LoginRequest
import id.codemockup.ramu.core.data.remote.response.auth.LoginResponse
import id.codemockup.ramu.core.domain.repository.auth.AuthRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repository: AuthRepository) {
    operator fun invoke(email: String, password: String): Flow<UiState<LoginResponse>> = flow {
        emit(UiState.Loading)
        val result = try {
            UiState.Success(repository.login(LoginRequest(email, password)).data)
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (error: Exception) {
            UiState.Error(error.message ?: "Unable to sign in. Try again.")
        }
        emit(result)
    }
}
