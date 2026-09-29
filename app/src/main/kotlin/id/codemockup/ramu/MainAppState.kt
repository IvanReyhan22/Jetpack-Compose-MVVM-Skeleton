package id.codemockup.ramu


sealed interface MainAppState {
    data object Loading : MainAppState
    data class Ready(val signedIn: Boolean) : MainAppState
    data object Error : MainAppState
}
