package id.codemockup.ramu.core.common


object ErrorMessageConstant {
    const val BAD_REQUEST = "Invalid request. Please check your input."
    const val ACCOUNT_NOT_FOUND = "Invalid email or password."
    const val SESSION_EXPIRED = "Your session has expired. Please sign in again."
    const val INTERNAL_SERVER_ERROR = "Server error. Please try again later."
    const val NO_CONNECTION = "Unable to connect. Check your internet connection."
    const val SERVER_UNREACHABLE = "Server could not be reached. Please try again."
    const val REQUEST_TIMEOUT = "Request timed out. Please try again."
    const val UNTRUSTED_CONNECTION = "A trusted connection could not be established."
    const val SESSION_STORAGE_ERROR = "Could not clear expired session. Please try again."
}
