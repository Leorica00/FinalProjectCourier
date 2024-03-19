package com.example.final_project.presentation.state

import com.example.final_project.presentation.model.chatbot.BotResponse

data class ChatBotMessageState(
    val messages: BotResponse? = null,
    val isLoading: Boolean = false,
    val errorMessage: Int? = null
)