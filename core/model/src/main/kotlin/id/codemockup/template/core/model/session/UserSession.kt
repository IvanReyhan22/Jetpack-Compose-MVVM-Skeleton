package id.codemockup.template.core.model.session


data class UserSession(val token: String, val user: User)
data class User(val id: String, val email: String)
