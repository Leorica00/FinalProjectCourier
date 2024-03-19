package com.example.finalprojectcourier.presentation.screen.chat.chat_messaging.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.final_project.data.remote.common.Resource
import com.example.finalprojectcourier.domain.usecase.chat.AddMessageUseCase
import com.example.finalprojectcourier.domain.usecase.chat.GetMessagesUseCase
import com.example.final_project.presentation.event.chat.ChatEvent
import com.example.final_project.presentation.mapper.chat.toDomain
import com.example.final_project.presentation.mapper.chat.toPresentation
import com.example.finalprojectcourier.presentation.model.chat.Message
import com.example.final_project.presentation.state.ChatState
import com.example.finalprojectcourier.presentation.util.getErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val getMessagesUseCase: GetMessagesUseCase, private val addMessageUseCase: AddMessageUseCase): ViewModel() {
    private val _chatStateFlow = MutableStateFlow(ChatState())
    val chatStateFlow = _chatStateFlow.asStateFlow()

    fun onEvent(event: ChatEvent) {
        when(event) {
            is ChatEvent.GetMessagesEvent -> getAllMessages(event.receiverUuid)
            is ChatEvent.AddMessageEvent -> addMessage(event.message, event.receiverUuid)
        }
    }

    private fun getAllMessages(receiverUuid: String) {
        viewModelScope.launch {
            getMessagesUseCase(receiverUuid).collect {resource ->
                when(resource) {
                    is Resource.Loading -> _chatStateFlow.update { currentState -> currentState.copy(isLoading = resource.loading) }
                    is Resource.Error -> _chatStateFlow.update { currentState -> currentState.copy(errorMessage = getErrorMessage(resource.error)) }
                    is Resource.Success -> _chatStateFlow.update { currentState -> currentState.copy(messages = resource.response.map { it.toPresentation() }.toMutableList())}
                }
            }
        }
    }

    fun getReceiverId(id: String) {
        _chatStateFlow.update { currentState -> currentState.copy(receiverId = id) }
    }

    private fun addMessage(message: Message, receiverUuid: String) {
        viewModelScope.launch {
            addMessageUseCase(message = message.toDomain(), receiverUuid = receiverUuid)
        }
    }
}