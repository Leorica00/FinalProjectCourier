package com.example.final_project.data.remote.mapper.chatbot

import com.example.final_project.data.remote.model.ChatBotRequestDto
import com.example.final_project.data.remote.model.QueryInputDto
import com.example.final_project.data.remote.model.TextDto
import com.example.final_project.domain.model.bot.PostChatBotModel

fun PostChatBotModel.toData(): ChatBotRequestDto {
    val textDto = TextDto(
        text = text,
        languageCode = "en-US"
    )
    val queryInputDto = QueryInputDto(
        text = textDto
    )
    return ChatBotRequestDto(
        queryInput = queryInputDto
    )
}