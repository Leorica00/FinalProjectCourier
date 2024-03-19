package com.example.finalprojectcourier.presentation.mapper.bot

import com.example.final_project.domain.model.bot.ChatBotResponse
import com.example.final_project.domain.model.bot.PostChatBotModel
import com.example.final_project.presentation.model.chatbot.BotRequest
import com.example.final_project.presentation.model.chatbot.BotResponse
import com.example.final_project.presentation.model.chatbot.SenderType

fun BotRequest.toDomain() : PostChatBotModel {
    return PostChatBotModel(text = text)
}

fun ChatBotResponse.toPresentation() : BotResponse {
    return BotResponse(
        responseId = responseId,
        response = response,
        senderType = SenderType.CHATBOT
    )
}