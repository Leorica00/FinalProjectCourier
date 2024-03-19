package com.example.final_project.domain.repository.auth

import com.example.final_project.data.remote.common.Resource
import kotlinx.coroutines.flow.Flow

interface FirebaseEmailLoginRepository {
    suspend fun firebaseSignInWithEmailAndPassword(email: String, password: String): Flow<Resource<Boolean>>

}