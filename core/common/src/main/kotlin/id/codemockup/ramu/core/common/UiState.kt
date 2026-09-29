package id.codemockup.ramu.core.common


sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

data class DataState<T>(
    val isLoading: Boolean = false,
    val data: T? = null,
    val errorMessage: String = "",
)
