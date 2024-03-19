package com.example.final_project.data.remote.model

data class MessageDto(
    val id: Long?,
    val message: String?,
    val senderId: String?
) {
    constructor() : this(null, null, null)
}
