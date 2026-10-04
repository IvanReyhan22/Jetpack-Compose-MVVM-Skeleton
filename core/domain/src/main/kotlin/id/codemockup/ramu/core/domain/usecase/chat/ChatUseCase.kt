package id.codemockup.ramu.core.domain.usecase.chat

data class ChatUseCase(
    val openChatUseCase: OpenChatUseCase,
    val sendChatUseCase: SendChatUseCase,
)
