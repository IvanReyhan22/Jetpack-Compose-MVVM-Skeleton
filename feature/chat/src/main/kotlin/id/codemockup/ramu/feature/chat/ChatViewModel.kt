package id.codemockup.ramu.feature.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.codemockup.ramu.core.common.DataState
import id.codemockup.ramu.core.common.UiState
import id.codemockup.ramu.core.domain.usecase.chat.ChatUseCase
import id.codemockup.ramu.core.model.chat.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val useCase: ChatUseCase) : ViewModel() {
    private val _state = MutableStateFlow(ChatState())
    val state = _state.asStateFlow()
    private var refreshing = false

    init {
        refresh()
    }

    fun editDraft(value: String) {
        _state.update { it.copy(draft = value) }
    }

    fun refresh() {
        if (refreshing || state.value.sending) return
        refreshing = true
        _state.update { it.copy(conversation = it.conversation.copy(isLoading = true)) }
        viewModelScope.launch {
            try {
                useCase.openChatUseCase().collect { result ->
                    _state.update { current ->
                        when (result) {
                            UiState.Loading -> current
                            is UiState.Success -> current.copy(
                                conversation = DataState(data = result.data),
                                needsRefresh = false
                            )

                            is UiState.Error -> current.copy(
                                conversation = current.conversation.copy(
                                    isLoading = false,
                                    errorMessage = result.message
                                ), needsRefresh = true
                            )
                        }
                    }
                }
            } finally {
                refreshing = false
            }
        }
    }

    fun send() {
        val current = state.value
        if (!current.canSend) return
        val conversation = current.conversation.data ?: return
        val input = current.draft.trim()
        val optimistic =
            ChatMessage(UUID.randomUUID().toString(), "user", input, System.currentTimeMillis())
        _state.update {
            it.copy(
                draft = "",
                sending = true,
                conversation = DataState(data = conversation.copy(messages = conversation.messages + optimistic))
            )
        }
        viewModelScope.launch {
            try {
                useCase.sendChatUseCase(conversation.sessionId, input).collect { result ->
                    when (result) {
                        UiState.Loading -> Unit
                        is UiState.Success -> _state.update {
                            it.copy(
                                sending = false,
                                conversation = DataState(data = result.data.copy(messages = it.conversation.data!!.messages + result.data.messages))
                            )
                        }

                        is UiState.Error -> {
                            _state.update {
                                it.copy(
                                    sending = false,
                                    needsRefresh = true,
                                    conversation = it.conversation.copy(errorMessage = result.message + " Delivery may have completed. Refresh history before sending again.")
                                )
                            }
                        }
                    }
                }
            } finally {
                _state.update { it.copy(sending = false) }
            }
        }
    }
}
