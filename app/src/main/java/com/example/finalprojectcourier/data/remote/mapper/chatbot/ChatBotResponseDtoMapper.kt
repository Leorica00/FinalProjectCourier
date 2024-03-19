package com.example.final_project.data.remote.mapper.chatbot

import com.example.final_project.data.remote.model.ChatBotResponseDto
import com.example.final_project.domain.model.bot.ChatBotResponse

fun ChatBotResponseDto.toDomain(): ChatBotResponse {
    return ChatBotResponse(
        responseId = this.responseId,
        response = this.queryResult.fulfillmentText
    )
}