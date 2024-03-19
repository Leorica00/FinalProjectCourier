package com.example.finalprojectcourier.domain.usecase.chat

import com.example.final_project.domain.model.chat.GetMessage
import com.example.final_project.domain.repository.chat.ChatMessagesRepository
import javax.inject.Inject

class AddMessageUseCase @Inject constructor(private val chatRepository: ChatMessagesRepository) {
    suspend operator fun invoke(message: GetMessage, receiverUuid: String) {
        chatRepository.addMessage(message, receiverUuid)
    }
}