package com.example.final_project.presentation.event.bot

sealed class ChatBotEvents {
    data class SendTextToChatBotEvent(val text: String) : ChatBotEvents()
    class UpdateSessionIdEvent(val sessionId: String) : ChatBotEvents()
    class UpdateErrorMessage(val errorMessage: Int?): ChatBotEvents()
}