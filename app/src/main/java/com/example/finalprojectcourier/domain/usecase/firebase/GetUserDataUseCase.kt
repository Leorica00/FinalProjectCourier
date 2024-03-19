package com.example.finalprojectcourier.domain.usecase.firebase

import com.example.final_project.data.remote.common.Resource
import com.example.final_project.domain.model.GetUserData
import com.example.final_project.domain.repository.firebase.FirebaseUserDataRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserDataUseCase @Inject constructor(private val firebaseUserDataRepository: FirebaseUserDataRepository) {
    suspend operator fun invoke(): Flow<Resource<GetUserData>> {
        return firebaseUserDataRepository.getUserData()
    }
}