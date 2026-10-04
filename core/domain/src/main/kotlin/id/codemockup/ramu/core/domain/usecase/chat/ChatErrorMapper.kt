package id.codemockup.ramu.core.domain.usecase.chat

import retrofit2.HttpException

internal fun chatErrorMessage(error: Exception): String = when ((error as? HttpException)?.code()) {
    401, 403 -> "Hermes rejected the API key. Check app.properties and rebuild."
    429 -> "Hermes is busy. Wait before sending another message."
    else -> error.message ?: "Could not reach Hermes. Check connection and refresh history."
}
