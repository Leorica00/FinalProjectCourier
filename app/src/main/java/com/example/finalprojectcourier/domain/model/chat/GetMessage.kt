package com.example.final_project.domain.model.chat

data class GetMessage(
    val id: Long?,
    val message: String?,
    val senderId: String?
) {
    constructor() : this(null, null, null)
}