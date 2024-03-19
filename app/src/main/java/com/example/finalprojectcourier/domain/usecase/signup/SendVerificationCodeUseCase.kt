package com.example.finalprojectcourier.domain.usecase.signup

import com.example.final_project.data.remote.common.Resource
import com.example.final_project.domain.repository.auth.FirebasePhoneAuthRepository
import com.google.firebase.auth.PhoneAuthOptions
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SendVerificationCodeUseCase @Inject constructor(private val firebasePhoneAuthRepository: FirebasePhoneAuthRepository) {
    suspend operator fun invoke(phoneNumber: String, options: PhoneAuthOptions.Builder) : Flow<Resource<String>> {
        return firebasePhoneAuthRepository.firebaseSendVerificationCodeToPhoneNumber(
            phoneNumber = phoneNumber,
            optionsBuilder = options
        )
    } 
}