package com.example.finalprojectcourier.domain.usecase.firebase

import com.example.final_project.data.remote.common.Resource
import com.example.final_project.domain.repository.firebase.FirebasePhotosRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RetrievePhotoUseCase @Inject constructor(private val firebasePhotosRepository: FirebasePhotosRepository) {
    suspend operator fun invoke() : Flow<Resource<String>> {
        return firebasePhotosRepository.getLastUserImage()
    }
}