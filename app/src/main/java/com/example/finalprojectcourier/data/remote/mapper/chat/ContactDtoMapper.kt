package com.example.final_project.data.remote.mapper.chat

import com.example.final_project.data.remote.model.ContactDto
import com.example.final_project.domain.model.chat.GetContact

fun ContactDto.toDomain() = GetContact(
    imageUrl = imageUrl,
    lastMessage = lastMessage,
    receiverId = receiverId,
    fullName = fullName
)

fun GetContact.toData() = ContactDto(
    imageUrl = imageUrl,
    lastMessage = lastMessage,
    receiverId = receiverId,
    fullName = fullName
)