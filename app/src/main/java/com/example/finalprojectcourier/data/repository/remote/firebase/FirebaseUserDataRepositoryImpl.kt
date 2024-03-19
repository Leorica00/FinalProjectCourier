package com.example.final_project.data.repository.remote.firebase

import com.example.final_project.data.remote.common.Resource
import com.example.final_project.data.remote.mapper.toDomain
import com.example.final_project.data.remote.model.UserDataDto
import com.example.final_project.di.DispatchersModule.IoDispatcher
import com.example.final_project.domain.model.GetUserData
import com.example.final_project.domain.repository.firebase.FirebaseUserDataRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FirebaseUserDataRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : FirebaseUserDataRepository {
    override val currentUser: FirebaseUser? get() = auth.currentUser

    override suspend fun getUserData(): Flow<Resource<GetUserData>> {
        return callbackFlow {
            trySend(Resource.Loading(true))

            currentUser?.let {
                    val email = it.email
                    val phone: String? = it.phoneNumber
                    val fullName = it.displayName
                    val userData = UserDataDto(email, phone, fullName)
                    trySend(Resource.Success(userData.toDomain()))
            }

            awaitClose()
        }.flowOn(ioDispatcher)
    }
}