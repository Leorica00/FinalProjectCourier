package com.example.finalprojectcourier.domain.usecase.chat

import com.example.final_project.domain.model.chat.GetContact
import com.example.final_project.domain.repository.chat.ChatContactsRepository
import javax.inject.Inject

class AddContactUseCase @Inject constructor(private val chatContactsRepository: ChatContactsRepository) {
    suspend operator fun invoke(contact: GetContact) = chatContactsRepository.addContact(contact)
}