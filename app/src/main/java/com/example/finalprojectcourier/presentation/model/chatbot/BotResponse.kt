package com.example.final_project.presentation.model.chatbot

data class BotResponse(
    val responseId: String,
    val response: String,
    val senderType: SenderType = SenderType.USER
)