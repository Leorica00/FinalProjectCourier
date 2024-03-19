package com.example.finalprojectcourier.domain.usecase.chat

import com.example.final_project.domain.repository.chat.ChatMessagesRepository
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(private val chatRepository: ChatMessagesRepository) {
    suspend operator fun invoke(receiverUuid: String) = chatRepository.getMessages(receiverUuid)
}