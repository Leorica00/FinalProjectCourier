package com.example.finalprojectcourier.presentation.screen.chat.chat_chatbot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.final_project.data.remote.common.Resource
import com.example.finalprojectcourier.domain.usecase.chatbot.SendTextToChatBotUseCase
import com.example.final_project.presentation.event.bot.ChatBotEvents
import com.example.finalprojectcourier.presentation.mapper.bot.toDomain
import com.example.finalprojectcourier.presentation.mapper.bot.toPresentation
import com.example.final_project.presentation.model.chatbot.BotRequest
import com.example.final_project.presentation.model.chatbot.BotResponse
import com.example.final_project.presentation.state.ChatBotState
import com.example.finalprojectcourier.presentation.util.getErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatBotViewModel @Inject constructor(private val sentTextToChatBotUseCase: SendTextToChatBotUseCase) : ViewModel() {
    private val _chatBotStateFlow = MutableStateFlow(ChatBotState())
    val chatBotStateFlow = _chatBotStateFlow.asStateFlow()

    fun onEvent(event: ChatBotEvents) {
        when(event) {
            is ChatBotEvents.SendTextToChatBotEvent -> sendMessageToChatBot(message = event.text)
            is ChatBotEvents.UpdateSessionIdEvent -> setSessionId(sessionId = event.sessionId)
            is ChatBotEvents.UpdateErrorMessage -> updateErrorMessage(errorMessage = event.errorMessage)
        }
    }

    private fun setSessionId(sessionId: String) {
        _chatBotStateFlow.update { currentState -> currentState.copy(sessionId = sessionId) }
    }

    private fun sendMessageToChatBot(message: String) {
        viewModelScope.launch {
            val updatedMessages: MutableList<BotResponse> = _chatBotStateFlow.value.messages?.toMutableList() ?: mutableListOf()
            updatedMessages.add(BotResponse(UUID.randomUUID().toString(), message))
            _chatBotStateFlow.update { currentState ->  currentState.copy(updatedMessages)}
            sentTextToChatBotUseCase(request = BotRequest(message).toDomain(), sessionId = _chatBotStateFlow.value.sessionId!!).collect {resource ->
                when(resource) {
                    is Resource.Success -> {
                        updatedMessages.add(resource.response.toPresentation())
                        _chatBotStateFlow.update { currentState -> currentState.copy(messages = updatedMessages) }
                    }

                    is Resource.Loading -> _chatBotStateFlow.update { currentState -> currentState.copy(isLoading = resource.loading) }

                    is Resource.Error -> updateErrorMessage(getErrorMessage(resource.error))
                }
            }
        }
    }

    private fun updateErrorMessage(errorMessage: Int?) {
        _chatBotStateFlow.update { currentState -> currentState.copy(errorMessage = errorMessage) }
    }
}