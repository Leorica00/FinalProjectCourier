package com.example.final_project.data.repository.remote.firebase

import android.net.Uri
import com.example.final_project.data.remote.common.HandleErrorStates
import com.example.final_project.data.remote.common.Resource
import com.example.final_project.domain.repository.firebase.FirebasePhotosRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebasePhotosRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firebaseStorage: FirebaseStorage
) : FirebasePhotosRepository {
    override val currentUser: FirebaseUser? get() = auth.currentUser
    override val storage: FirebaseStorage get() = firebaseStorage

    override suspend fun uploadImage(imageUri: Uri): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading(true))

            val userId = currentUser?.uid ?: throw Exception()
            val timestamp = System.currentTimeMillis()

            val fileName = "${timestamp}_${imageUri.lastPathSegment}"

            val storageRef = storage.reference.child("user_images/$userId/$fileName")
            val uploadTask = storageRef.putFile(imageUri).await()
            val imageUrl = uploadTask.storage.downloadUrl.await().toString()
            emit(Resource.Success(imageUrl))
        } catch (t: Exception) {
            emit(Resource.Error(HandleErrorStates.handleException(t), throwable = t))
        }
    }

    override suspend fun getLastUserImage(): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading(true))

            val userId = currentUser?.uid ?: throw Exception("User not authenticated")
            val imagesRef = storage.reference.child("user_images/$userId")
            val imagesList = imagesRef.listAll().await()

            if (imagesList.items.isNotEmpty()) {
                val sortedImagesList = imagesList.items.sortedByDescending { it.name }
                val lastImageRef = sortedImagesList.first()
                val imageUrl = lastImageRef.downloadUrl.await().toString()

                emit(Resource.Success(imageUrl))
            } else {
                emit(Resource.Success(""))
            }
        } catch (t: Exception) {
            emit(Resource.Error(HandleErrorStates.handleException(t), throwable = t))
        }
    }
}