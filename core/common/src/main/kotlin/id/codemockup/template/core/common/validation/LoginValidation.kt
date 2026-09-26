package id.codemockup.template.core.common.validation


object LoginValidation {
    fun emailError(email: String): String? = when {
        email.isBlank() -> "Enter your email."
        !email.trim().matches(Regex("[^\\s@]+@[^\\s@]+\\.[^\\s@]+")) -> "Enter a valid email."
        else -> null
    }

    fun passwordError(password: String): String? =
        if (password.isEmpty()) "Enter your password." else null
}
