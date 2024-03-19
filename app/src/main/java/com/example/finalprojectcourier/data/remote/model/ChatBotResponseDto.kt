package com.example.final_project.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChatBotResponseDto(
    @Json(name = "responseId") val responseId: String,
    @Json(name = "queryResult") val queryResult: QueryResultDto
)

@JsonClass(generateAdapter = true)
data class QueryResultDto(
    @Json(name = "fulfillmentText") val fulfillmentText: String
)