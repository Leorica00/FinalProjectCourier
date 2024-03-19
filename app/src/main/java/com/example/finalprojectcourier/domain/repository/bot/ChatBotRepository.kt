package com.example.final_project.domain.repository.bot

import com.example.final_project.data.remote.common.Resource
import com.example.final_project.domain.model.bot.ChatBotResponse
import com.example.final_project.domain.model.bot.PostChatBotModel
import kotlinx.coroutines.flow.Flow

interface ChatBotRepository {
    suspend fun sendRequest(request: PostChatBotModel, sessionId: String) : Flow<Resource<ChatBotResponse>>
}