package com.example.final_project.data.remote.mapper.chat

import com.example.final_project.data.remote.model.MessageDto
import com.example.final_project.domain.model.chat.GetMessage

fun MessageDto.toDomain() = GetMessage(
    id = id,
    message = message,
    senderId = senderId
)

fun GetMessage.toData() = MessageDto(
    id = id,
    message = message,
    senderId = senderId
)