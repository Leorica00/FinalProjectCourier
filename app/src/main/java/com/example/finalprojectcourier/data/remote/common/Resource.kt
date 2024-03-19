package com.example.final_project.data.remote.common

import java.lang.Exception

sealed class Resource<T: Any> {
    data class Loading<T: Any>(val loading: Boolean) : Resource<T>()
    data class Success<T: Any>(val response: T) : Resource<T>()
    data class Error<T: Any>(val error: HandleErrorStates, val throwable: Exception) : Resource<T>()
}