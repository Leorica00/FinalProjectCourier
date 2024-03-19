package com.example.finalprojectcourier.domain.usecase.signup

import com.example.final_project.data.remote.common.Resource
import com.example.final_project.domain.repository.auth.FirebasePhoneAuthRepository
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.PhoneAuthCredential
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignInWithAuthCredentialUseCase @Inject constructor(private val firebasePhoneAuthRepository: FirebasePhoneAuthRepository) {
    suspend operator fun invoke(credential: PhoneAuthCredential) : Flow<Resource<AuthResult>> {
        return firebasePhoneAuthRepository.signInWithPhoneAuthCredential(credential = credential)
    }
}