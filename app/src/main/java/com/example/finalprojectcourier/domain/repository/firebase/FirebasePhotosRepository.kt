package com.example.final_project.domain.repository.firebase

import android.net.Uri
import com.example.final_project.data.remote.common.Resource
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow

interface FirebasePhotosRepository {
    val currentUser: FirebaseUser?
    val storage: FirebaseStorage

    suspend fun uploadImage(imageUri: Uri): Flow<Resource<String>>
    suspend fun getLastUserImage(): Flow<Resource<String>>
}