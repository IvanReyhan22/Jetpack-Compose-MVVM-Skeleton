package id.codemockup.ramu.core.domain.mapper

import id.codemockup.ramu.core.data.remote.response.chat.ChatMessageDto
import id.codemockup.ramu.core.model.chat.ChatMessage

fun ChatMessageDto.toChatMessage(index: Int): ChatMessage? {
    if (role !in listOf("user", "assistant") || display_kind in listOf("commentary", "hidden", "process_complete")) return null
    val text = when (val value = content) {
        is String -> value
        is List<*> -> value.mapNotNull { part ->
            val item = part as? Map<*, *> ?: return@mapNotNull null
            if (item["type"] in listOf("text", "output_text", "input_text")) item["text"] as? String else null
        }.joinToString("\n")
        else -> ""
    }
    return text.takeIf { it.isNotBlank() }?.let {
        ChatMessage(
            id ?: "history-$index",
            role,
            it,
            timestamp?.takeIf { seconds -> seconds.isFinite() }?.let { seconds -> (seconds * 1000).toLong() },
        )
    }
}
