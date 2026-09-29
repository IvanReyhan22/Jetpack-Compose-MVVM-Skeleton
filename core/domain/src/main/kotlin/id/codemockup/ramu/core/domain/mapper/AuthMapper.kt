package id.codemockup.ramu.core.domain.mapper


import id.codemockup.ramu.core.data.remote.response.auth.LoginResponse
import id.codemockup.ramu.core.model.session.User
import id.codemockup.ramu.core.model.session.UserSession

fun LoginResponse.toSession(): UserSession = UserSession(token, User(userId, email))
