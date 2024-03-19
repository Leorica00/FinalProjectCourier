package com.example.finalprojectcourier.domain.usecase.firebase

import com.example.final_project.data.remote.common.Resource
import com.example.final_project.domain.repository.auth.FirebaseSignOutRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignOutUseCase @Inject constructor(private val firebaseSignOutRepository: FirebaseSignOutRepository) {
    operator fun invoke() : Flow<Resource<Unit>> {
        return firebaseSignOutRepository.signOut()
    }
}