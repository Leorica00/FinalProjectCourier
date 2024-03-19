package com.example.final_project.presentation.event.chat

import com.example.final_project.presentation.model.chat.Contact

sealed interface ChatContactEvent {
    object GetContactsEvent: ChatContactEvent
    class AddContactEvent(val contact: Contact): ChatContactEvent
}