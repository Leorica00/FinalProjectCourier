package com.example.final_project.domain.repository.auth

import com.example.final_project.data.remote.common.Resource
import com.example.final_project.domain.model.GetUserAdditionalData
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface FirebaseAdditionalUserDataRepository {
    val currentUser: FirebaseUser?

    suspend fun addUserFullName(userData: GetUserAdditionalData) : Flow<Resource<Boolean>>
    suspend fun addUserEmail(userData: GetUserAdditionalData) : Flow<Resource<Boolean>>
    suspend fun addUserPassword(userData: GetUserAdditionalData) : Flow<Resource<Boolean>>
}