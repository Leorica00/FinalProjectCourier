package com.example.final_project.domain.repository.auth

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface FirebaseAuthStateRepository {
    val currentUser: FirebaseUser?

    fun getAuthState(): Flow<Boolean>
}