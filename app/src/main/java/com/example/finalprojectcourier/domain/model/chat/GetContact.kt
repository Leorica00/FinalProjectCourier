package com.example.final_project.domain.model.chat

data class GetContact(
    val imageUrl: String? = null,
    val lastMessage: String?,
    val receiverId: String?,
    val fullName: String?
)
