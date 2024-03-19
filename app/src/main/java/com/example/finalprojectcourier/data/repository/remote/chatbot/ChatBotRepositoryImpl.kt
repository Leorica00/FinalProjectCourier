package com.example.finalprojectcourier.data.repository.remote.chatbot

import android.util.Log.d
import com.example.final_project.data.remote.common.Resource
import com.example.final_project.data.remote.common.ResponseHandler
import com.example.final_project.data.remote.mapper.base.asResource
import com.example.final_project.data.remote.mapper.chatbot.toData
import com.example.final_project.data.remote.mapper.chatbot.toDomain
import com.example.final_project.data.remote.service.ChatBotApiService
import com.example.final_project.domain.model.bot.ChatBotResponse
import com.example.final_project.domain.model.bot.PostChatBotModel
import com.example.final_project.domain.repository.bot.ChatBotRepository
import com.example.finalprojectcourier.data.local.datasource.ChatBotAuthTokenDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatBotRepositoryImpl @Inject constructor(
    private val handler: ResponseHandler,
    private val chatBotApiService: ChatBotApiService,
    private val chatBotAuthTokenDataSource: ChatBotAuthTokenDataSource
) : ChatBotRepository {
    override suspend fun sendRequest(
        request: PostChatBotModel,
        sessionId: String
    ): Flow<Resource<ChatBotResponse>> {
        d("newTokenBro", chatBotAuthTokenDataSource.token)
        return handler.safeApiCall {
            chatBotApiService.postRequest(sessionId = sessionId, body = request.toData(), "Bearer ${chatBotAuthTokenDataSource.token}")
        }.asResource {
            it.toDomain()
        }
    }
}