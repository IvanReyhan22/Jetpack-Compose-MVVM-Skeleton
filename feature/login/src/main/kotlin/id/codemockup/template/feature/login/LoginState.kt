package id.codemockup.template.feature.login


import id.codemockup.template.core.common.DataState
import id.codemockup.template.core.data.remote.response.auth.LoginResponse

data class LoginState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val login: DataState<LoginResponse> = DataState(),
)

sealed interface LoginEffect {
    data object SignedIn : LoginEffect
}
