package com.example.final_project.presentation.mapper.chat

import com.example.final_project.domain.model.chat.GetContact
import com.example.final_project.presentation.model.chat.Contact

fun Contact.toDomain() = GetContact(
    imageUrl = imageUrl,
    lastMessage = lastMessage,
    receiverId = receiverId,
    fullName = fullName
)

fun GetContact.toPresentation() = Contact(
    imageUrl = imageUrl,
    lastMessage = lastMessage,
    receiverId = receiverId,
    fullName = fullName
)