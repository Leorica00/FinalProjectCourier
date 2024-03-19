package com.example.final_project.data.remote.common

import com.example.final_project.di.DispatchersModule.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class EmailSignInResponseHandler @Inject constructor(@IoDispatcher private val ioDispatcher: CoroutineDispatcher) {
    suspend fun <T> safeAuthCall(call: suspend () -> T): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading(true))

        try {
            call()
            emit(Resource.Success(true))
        } catch (t: Exception) {
            emit(Resource.Error(error = HandleErrorStates.handleException(t), throwable = t))
        }

        emit(Resource.Loading(false))
    }.flowOn(ioDispatcher)
}