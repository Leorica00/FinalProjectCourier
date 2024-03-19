package com.example.final_project.data.repository.remote.chat

import com.example.final_project.data.remote.common.HandleErrorStates
import com.example.final_project.data.remote.common.Resource
import com.example.final_project.data.remote.mapper.chat.toData
import com.example.final_project.data.remote.mapper.chat.toDomain
import com.example.final_project.data.remote.model.MessageDto
import com.example.final_project.domain.model.chat.GetMessage
import com.example.final_project.domain.repository.chat.ChatMessagesRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class ChatMessagesRepositoryImpl @Inject constructor(private val auth: FirebaseAuth, private val databaseReference: DatabaseReference) :
    ChatMessagesRepository {

    override val currentUser = auth.currentUser

    override suspend fun getMessages(receiverUuid: String): Flow<Resource<List<GetMessage>>> = callbackFlow {
        trySend(Resource.Loading(loading = true))
        val senderRoom = receiverUuid.plus(currentUser?.uid)
        databaseReference.child("chats").child(senderRoom).child("messages")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val listOfMessages = mutableListOf<MessageDto>()
                    snapshot.children.forEach {
                        listOfMessages.add(it.getValue(MessageDto::class.java)!!)
                    }

                    trySend(Resource.Success(response = listOfMessages.map { it.toDomain() }))
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        trySend(Resource.Loading(loading = false))
        awaitClose {

        }
    }.catch {
        emit(Resource.Error(HandleErrorStates.handleException(it as Exception), throwable = it))
    }.flowOn(IO)

    override suspend fun addMessage(message: GetMessage, receiverUuid: String): Unit = withContext(IO) {
        currentUser?.uid?.let {uid ->
            val senderRoom = "$receiverUuid$uid"
            val receiverRoom = "$uid$receiverUuid"

            val messageId = databaseReference.child("chats").child(senderRoom).child("messages").push().key
            messageId?.let {
                val senderMessageRef = databaseReference.child("chats").child(senderRoom).child("messages").child(it)
                val receiverMessageRef = databaseReference.child("chats").child(receiverRoom).child("messages").child(it)

                senderMessageRef.setValue(message.toData())
                    .addOnSuccessListener {
                        receiverMessageRef.setValue(message.toData())
                    }
                    .addOnFailureListener { exception ->

                    }
            }
        }
    }
}