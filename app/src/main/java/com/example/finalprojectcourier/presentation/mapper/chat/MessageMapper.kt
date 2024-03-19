package com.example.final_project.presentation.mapper.chat

import com.example.final_project.domain.model.chat.GetMessage
import com.example.finalprojectcourier.presentation.model.chat.Message

fun Message.toDomain() = GetMessage(
    id = id,
    message = message,
    senderId = senderId
)

fun GetMessage.toPresentation() = Message(
    id = id,
    message = message,
    senderId = senderId
)