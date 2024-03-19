package com.example.final_project.domain.repository.chat

import com.example.final_project.data.remote.common.Resource
import com.example.final_project.domain.model.chat.GetMessage
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface ChatMessagesRepository {
    val currentUser: FirebaseUser?
    suspend fun getMessages(receiverUuid: String): Flow<Resource<List<GetMessage>>>

    suspend fun addMessage(message: GetMessage, receiverUuid: String)
}