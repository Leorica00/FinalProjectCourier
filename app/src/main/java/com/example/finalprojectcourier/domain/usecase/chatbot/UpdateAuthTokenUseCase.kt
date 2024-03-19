package com.example.finalprojectcourier.domain.usecase.chatbot

import com.example.finalprojectcourier.data.local.datasource.ChatBotAuthTokenDataSource
import javax.inject.Inject

class UpdateAuthTokenUseCase @Inject constructor(private val chatBotAuthTokenDataSource: ChatBotAuthTokenDataSource) {
    suspend operator fun invoke() = chatBotAuthTokenDataSource.updateToken()
}