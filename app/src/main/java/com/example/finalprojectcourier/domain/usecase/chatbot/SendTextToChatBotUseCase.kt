package com.example.finalprojectcourier.domain.usecase.chatbot

import com.example.final_project.data.remote.common.Resource
import com.example.final_project.domain.model.bot.ChatBotResponse
import com.example.final_project.domain.model.bot.PostChatBotModel
import com.example.final_project.domain.repository.bot.ChatBotRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SendTextToChatBotUseCase @Inject constructor(private val chatBotRepository: ChatBotRepository) {
    suspend operator fun invoke(request: PostChatBotModel, sessionId: String) : Flow<Resource<ChatBotResponse>> {
        return chatBotRepository.sendRequest(request = request, sessionId = sessionId)
    }
}