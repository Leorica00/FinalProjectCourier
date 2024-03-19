package com.example.final_project.data.remote.model

data class ContactDto(
    val imageUrl: String? = null,
    val lastMessage: String?,
    val receiverId: String?,
    val fullName: String?
){
    constructor() : this(null, null, null, null)
}