package com.example.final_project.presentation.state

import com.example.final_project.presentation.model.chatbot.BotResponse

data class ChatBotState(
    val messages: List<BotResponse>? = null,
    val sessionId: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: Int? = null
)
