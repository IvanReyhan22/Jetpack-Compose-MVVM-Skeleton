package id.codemockup.template.core.network


import com.google.gson.JsonParser
import id.codemockup.template.core.common.ErrorMessageConstant

object HttpErrorMapper {
    fun message(code: Int, body: String, authenticated: Boolean): String {
        if (code == 401) return if (authenticated) ErrorMessageConstant.SESSION_EXPIRED
        else ErrorMessageConstant.ACCOUNT_NOT_FOUND
        val fallback = when (code) {
            400 -> ErrorMessageConstant.BAD_REQUEST
            in 500..599 -> ErrorMessageConstant.INTERNAL_SERVER_ERROR
            else -> "Request failed (HTTP $code)."
        }
        return try {
            val json = JsonParser.parseString(body).asJsonObject
            val field = if (code == 400) "data" else "message"
            json.get(field)?.takeIf { it.isJsonPrimitive && it.asJsonPrimitive.isString }
                ?.asString?.takeIf { it.isNotBlank() } ?: fallback
        } catch (_: Exception) {
            fallback
        }
    }
}
