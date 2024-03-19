package com.example.final_project.data.repository.remote.firebase

import com.example.final_project.di.DispatchersModule.IoDispatcher
import com.example.final_project.domain.repository.auth.FirebaseAuthStateRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FirebaseAuthStateRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : FirebaseAuthStateRepository {
    override val currentUser: FirebaseUser? get() = auth.currentUser

    override fun getAuthState(): Flow<Boolean> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val isUserAuthenticated = firebaseAuth.currentUser != null
            trySend(isUserAuthenticated)
        }
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener)

        awaitClose {
            FirebaseAuth.getInstance().removeAuthStateListener(authStateListener)
        }
    }.flowOn(ioDispatcher)
}