package com.example.final_project.domain.repository.auth

import com.example.final_project.data.remote.common.Resource
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface FirebaseSignOutRepository {
    val currentUser: FirebaseUser?

    fun signOut() : Flow<Resource<Unit>>
}