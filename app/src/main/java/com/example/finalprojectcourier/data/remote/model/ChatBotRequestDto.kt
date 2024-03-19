package com.example.final_project.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChatBotRequestDto(
    @Json(name = "query_input") val queryInput: QueryInputDto
)

@JsonClass(generateAdapter = true)
data class QueryInputDto(
    @Json(name = "text") val text: TextDto
)

@JsonClass(generateAdapter = true)
data class TextDto(
    @Json(name = "text") val text: String,
    @Json(name = "language_code") val languageCode: String
)

