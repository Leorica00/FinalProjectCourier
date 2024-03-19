package com.example.finalprojectcourier.domain.usecase.signup

import com.example.final_project.data.remote.common.Resource
import com.example.final_project.domain.repository.auth.FirebaseEmailLoginRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignInWithEmailPasswordUseCase @Inject constructor(private val firebaseEmailLoginRepository: FirebaseEmailLoginRepository) {
    suspend operator fun invoke(email: String, password: String) : Flow<Resource<Boolean>> {
        return firebaseEmailLoginRepository.firebaseSignInWithEmailAndPassword(email, password)
    }
}