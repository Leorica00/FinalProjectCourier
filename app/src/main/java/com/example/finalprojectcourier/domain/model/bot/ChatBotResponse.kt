package com.example.final_project.domain.model.bot

import com.example.final_project.data.remote.model.QueryResultDto
import com.squareup.moshi.Json

data class ChatBotResponse(
    val responseId: String,
    val response: String
)