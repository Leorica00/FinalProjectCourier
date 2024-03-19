package com.example.final_project.domain.repository.firebase

import com.example.final_project.data.remote.common.Resource
import com.example.final_project.domain.model.GetUserData
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface FirebaseUserDataRepository {
    val currentUser: FirebaseUser?

    suspend fun getUserData() : Flow<Resource<GetUserData>>
}