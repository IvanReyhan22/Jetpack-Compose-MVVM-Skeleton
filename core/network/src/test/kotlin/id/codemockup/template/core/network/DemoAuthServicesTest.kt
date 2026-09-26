package id.codemockup.template.core.network

import id.codemockup.template.core.data.remote.request.LoginRequest
import id.codemockup.template.core.network.demo.DemoAuthServices
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class DemoAuthServicesTest {
    @Test fun acceptsOnlyDocumentedDemoCredentials() = runTest {
        val service = DemoAuthServices()
        val response = service.login(LoginRequest("demo@example.com", "password123"))
        assertEquals("demo@example.com", response.data.email)
        assertTrue(response.data.token.isNotEmpty())
        try {
            service.login(LoginRequest("demo@example.com", "wrong"))
            fail("Expected credential failure")
        } catch (error: IllegalArgumentException) {
            assertEquals("Invalid email or password.", error.message)
        }
    }
}
