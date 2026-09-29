package id.codemockup.ramu.core.common

import id.codemockup.ramu.core.common.validation.LoginValidation
import org.junit.Assert.*
import org.junit.Test

class LoginValidationTest {
    @Test fun validatesRequiredFieldsAndEmail() {
        assertNotNull(LoginValidation.emailError(""))
        assertNotNull(LoginValidation.emailError("demo"))
        assertNotNull(LoginValidation.emailError("de mo@example.com"))
        assertNull(LoginValidation.emailError("demo@example.com"))
        assertNull(LoginValidation.emailError("  demo@example.com  "))
        assertNotNull(LoginValidation.passwordError(""))
        assertNull(LoginValidation.passwordError("password123"))
    }
}
