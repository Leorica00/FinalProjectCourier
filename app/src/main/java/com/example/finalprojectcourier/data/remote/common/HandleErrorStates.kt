package com.example.final_project.data.remote.common

import android.util.Log
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.TimeoutException

sealed class HandleErrorStates(val errorCode: ErrorCode) {
    data class ClientError(val code: Int, val errorBody: String = "") : HandleErrorStates(ErrorCode.CLIENT_ERROR)
    object ServerError : HandleErrorStates(ErrorCode.SERVER_ERROR)
    data class HttpError(val httpCode: Int, val errorBody: String = "") : HandleErrorStates(
        ErrorCode.HTTP_ERROR
    )
    object NetworkError : HandleErrorStates(ErrorCode.NETWORK_ERROR)
    object TimeoutError : HandleErrorStates(ErrorCode.TIMEOUT_ERROR)
    object UnknownError : HandleErrorStates(ErrorCode.UNKNOWN_ERROR)
    object FirebaseCredentialError: HandleErrorStates(ErrorCode.INVALID_CREDENTIALS)

    enum class ErrorCode {
        CLIENT_ERROR, SERVER_ERROR, HTTP_ERROR, NETWORK_ERROR, TIMEOUT_ERROR, UNKNOWN_ERROR, INVALID_CREDENTIALS
    }

    companion object {
        fun handleException(throwable: Exception): HandleErrorStates {
            Log.d("RACXA ERROR", throwable.stackTraceToString())

            return when (throwable) {
                is IOException -> NetworkError
                is HttpException -> when (throwable.code()) {
                    in 400..499 -> ClientError(throwable.code(), throwable.response()?.errorBody()?.string() ?: "")
                    in 500..599 -> ServerError
                    else -> HttpError(throwable.code(), throwable.response()?.errorBody()?.string() ?: "")
                }
                is TimeoutException -> TimeoutError
                is FirebaseAuthInvalidCredentialsException -> FirebaseCredentialError
                is FirebaseNetworkException -> NetworkError
                else -> UnknownError
            }
        }
    }
}