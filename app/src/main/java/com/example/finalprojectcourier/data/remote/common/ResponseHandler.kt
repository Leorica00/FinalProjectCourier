package com.example.final_project.data.remote.common

import android.util.Log.d
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import retrofit2.HttpException
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

class ResponseHandler @Inject constructor() {
    suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): Flow<Resource<T>> = flow {
        emit(Resource.Loading(true))
        d("responseBody", call().toString())


        val response = call()
        val responseBody = response.body()

        if (response.isSuccessful && responseBody != null) {
            emit(Resource.Success(responseBody))
        } else {
            throw HttpException(response)
        }
    }.catch { e ->
        emit(Resource.Error(error = HandleErrorStates.handleException(e as Exception), throwable = e))
    }.onCompletion {
        emit(Resource.Loading(false))
    }
}