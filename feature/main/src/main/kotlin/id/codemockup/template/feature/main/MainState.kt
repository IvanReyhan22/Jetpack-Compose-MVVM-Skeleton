package id.codemockup.template.feature.main


data class MainState(val email: String = "", val isLoading: Boolean = true, val error: String = "")
sealed interface MainEffect {
    data object SignedOut : MainEffect
}
