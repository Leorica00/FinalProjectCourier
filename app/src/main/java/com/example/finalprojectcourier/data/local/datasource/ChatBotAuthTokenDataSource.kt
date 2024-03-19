package com.example.finalprojectcourier.data.local.datasource

import android.content.Context
import com.google.auth.oauth2.GoogleCredentials
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChatBotAuthTokenDataSource @Inject constructor(@ApplicationContext val context: Context) {
    var token: String = ""

    suspend fun updateToken() = withContext((IO)) {
        val assetManager = context.assets
        val credentialsStream = assetManager.open("finalproject-4ba60-0c479db090b9.json")
        val credentials = GoogleCredentials.fromStream(credentialsStream)
        val scopedCredentials = credentials.createScoped(listOf("https://www.googleapis.com/auth/dialogflow"))
        val accessToken = scopedCredentials.refreshAccessToken()
        token = accessToken.tokenValue
    }
}