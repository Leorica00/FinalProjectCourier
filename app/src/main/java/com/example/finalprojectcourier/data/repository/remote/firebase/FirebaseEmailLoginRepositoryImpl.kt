package com.example.final_project.data.repository.remote.firebase

import com.example.final_project.data.remote.common.EmailSignInResponseHandler
import com.example.final_project.data.remote.common.Resource
import com.example.final_project.domain.repository.auth.FirebaseEmailLoginRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseEmailLoginRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val emailSignInResponseHandler: EmailSignInResponseHandler
) : FirebaseEmailLoginRepository {
    override suspend fun firebaseSignInWithEmailAndPassword(email: String, password: String): Flow<Resource<Boolean>> {
        return emailSignInResponseHandler.safeAuthCall {
            auth.signInWithEmailAndPassword(email, password).await()
        }
    }
}