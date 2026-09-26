package id.codemockup.template.core.domain.mapper


import id.codemockup.template.core.data.remote.response.auth.LoginResponse
import id.codemockup.template.core.model.session.User
import id.codemockup.template.core.model.session.UserSession

fun LoginResponse.toSession(): UserSession = UserSession(token, User(userId, email))
