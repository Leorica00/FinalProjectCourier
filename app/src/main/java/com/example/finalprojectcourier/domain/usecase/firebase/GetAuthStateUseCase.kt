package com.example.finalprojectcourier.domain.usecase.firebase

import com.example.final_project.domain.repository.auth.FirebaseAuthStateRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAuthStateUseCase @Inject constructor(private val authStateRepository: FirebaseAuthStateRepository) {
    operator fun invoke() : Flow<Boolean> = authStateRepository.getAuthState()
}