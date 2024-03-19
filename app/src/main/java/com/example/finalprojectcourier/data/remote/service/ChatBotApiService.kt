package com.example.final_project.data.remote.service

import com.example.final_project.data.remote.model.ChatBotRequestDto
import com.example.final_project.data.remote.model.ChatBotResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatBotApiService {
    @Headers(
        "x-goog-user-project: finalproject-4ba60"
    )
    @POST("/v2/projects/finalproject-4ba60/agent/sessions/{sessionId}:detectIntent")
    suspend fun postRequest(@Path("sessionId") sessionId: String, @Body body: ChatBotRequestDto, @Header("Authorization") authToken: String) : Response<ChatBotResponseDto>
}