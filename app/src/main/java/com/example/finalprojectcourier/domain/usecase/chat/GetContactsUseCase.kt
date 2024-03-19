package com.example.finalprojectcourier.domain.usecase.chat

import com.example.final_project.domain.repository.chat.ChatContactsRepository
import javax.inject.Inject

class GetContactsUseCase @Inject constructor(private val chatContactsRepository: ChatContactsRepository) {
    suspend operator fun invoke() = chatContactsRepository.getContacts()
}