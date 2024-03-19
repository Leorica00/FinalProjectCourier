package com.example.final_project.data.repository.remote.firebase

import com.example.final_project.data.remote.common.HandleErrorStates
import com.example.final_project.data.remote.common.Resource
import com.example.final_project.di.DispatchersModule.IoDispatcher
import com.example.final_project.domain.repository.auth.FirebaseSignOutRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FirebaseSignOutRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : FirebaseSignOutRepository {
    override val currentUser: FirebaseUser? get() = auth.currentUser

    override fun signOut() : Flow<Resource<Unit>> {
        return callbackFlow {
            trySend(Resource.Loading(true))

            try {
                auth.signOut()
                trySend(Resource.Success(response = Unit))
            } catch (e: Exception) {
                trySend(Resource.Error(error = HandleErrorStates.handleException(throwable = e), throwable = e))
            }

            awaitClose {}
        }.flowOn(ioDispatcher)
    }
}