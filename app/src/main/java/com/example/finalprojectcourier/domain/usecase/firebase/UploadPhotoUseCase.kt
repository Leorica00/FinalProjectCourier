package com.example.finalprojectcourier.domain.usecase.firebase

import android.net.Uri
import com.example.final_project.data.remote.common.Resource
import com.example.final_project.domain.repository.firebase.FirebasePhotosRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UploadPhotoUseCase @Inject constructor(private val photoUploadRepository: FirebasePhotosRepository) {
    suspend operator fun invoke(imageUri: Uri) : Flow<Resource<String>> {
        return photoUploadRepository.uploadImage(imageUri = imageUri)
    }
}