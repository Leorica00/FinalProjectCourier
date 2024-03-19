package com.example.final_project.data.repository.remote.firebase

import com.example.final_project.data.remote.common.HandleErrorStates
import com.example.final_project.data.remote.common.Resource
import com.example.final_project.di.DispatchersModule.IoDispatcher
import com.example.final_project.domain.repository.auth.FirebasePhoneAuthRepository
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FirebasePhonePhoneAuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : FirebasePhoneAuthRepository {

    override suspend fun firebaseSendVerificationCodeToPhoneNumber(phoneNumber: String, optionsBuilder: PhoneAuthOptions.Builder): Flow<Resource<String>> {
        return callbackFlow {
            trySend(Resource.Loading(true))

            val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                }

                override fun onVerificationFailed(exception: FirebaseException) {
                    trySend(Resource.Error(error = HandleErrorStates.handleException(exception), throwable = exception))
                }

                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    trySend(Resource.Success(response = verificationId))
                }
            }

            val options = optionsBuilder.setCallbacks(callbacks).build()
            PhoneAuthProvider.verifyPhoneNumber(options)

            awaitClose {}
        }.flowOn(ioDispatcher)
    }

    override suspend fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential): Flow<Resource<AuthResult>> {
        return callbackFlow<Resource<AuthResult>> {
            auth.signInWithCredential(credential).addOnCompleteListener { task ->
                trySend(Resource.Loading(true))
                if (task.isSuccessful) {
                    trySend(Resource.Success(response = task.result))
                } else {
                    trySend(Resource.Error(error = HandleErrorStates.handleException(throwable = task.exception!!), throwable = task.exception!!))
                }
            }

            awaitClose {}
        }.flowOn(ioDispatcher)
    }
}