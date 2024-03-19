package com.example.final_project.data.repository.remote.firebase

import com.example.final_project.data.remote.common.HandleErrorStates
import com.example.final_project.data.remote.common.Resource
import com.example.final_project.di.DispatchersModule.IoDispatcher
import com.example.final_project.domain.model.GetUserAdditionalData
import com.example.final_project.domain.repository.auth.FirebaseAdditionalUserDataRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FirebaseAdditionalUserDataRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : FirebaseAdditionalUserDataRepository {
    override val currentUser: FirebaseUser? get() = auth.currentUser
    override suspend fun addUserFullName(userData: GetUserAdditionalData): Flow<Resource<Boolean>> {
        return callbackFlow<Resource<Boolean>> {
            trySend(Resource.Loading(true))

            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(userData.fullName)
                .build()

            currentUser?.updateProfile(profileUpdates)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        trySend(Resource.Success(true))
                    } else {
                        trySend(Resource.Error(error = HandleErrorStates.handleException(task.exception!!), throwable = task.exception!!))
                    }
                }

            trySend(Resource.Loading(false))
            awaitClose {}
        }.flowOn(ioDispatcher)
    }

    override suspend fun addUserEmail(userData: GetUserAdditionalData): Flow<Resource<Boolean>> {
        return callbackFlow<Resource<Boolean>> {
            trySend(Resource.Loading(true))

            currentUser?.verifyBeforeUpdateEmail(userData.email)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        trySend(Resource.Success(true))
                    } else {
                        trySend(Resource.Error(
                                error = HandleErrorStates.handleException(task.exception!!),
                                throwable = task.exception!!)
                        )
                    }
                }

            trySend(Resource.Loading(false))
            awaitClose {}
        }.flowOn(ioDispatcher)
    }

    override suspend fun addUserPassword(userData: GetUserAdditionalData): Flow<Resource<Boolean>> {
        return callbackFlow<Resource<Boolean>> {
            trySend(Resource.Loading(true))

            currentUser?.updatePassword(userData.password)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        trySend(Resource.Success(true))
                    } else {
                        trySend(
                            Resource.Error(
                                error = HandleErrorStates.handleException(task.exception!!),
                                throwable = task.exception!!
                            )
                        )
                    }
                }

            trySend(Resource.Loading(false))
            awaitClose {}
        }.flowOn(ioDispatcher)
    }
}