package com.example.finalprojectcourier.presentation.model.chat

data class Message(
    val id: Long?,
    val message: String?,
    val senderId: String?
) {
    constructor() : this(null, null, null)
}
