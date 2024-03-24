package com.example.finalprojectcourier.data.remote.service

import com.example.finalprojectcourier.data.remote.model.distance.DistanceMatrixDto
import com.example.finalprojectcourier.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleDistanceMatrixApiService {
    @GET("/maps/api/distancematrix/json")
    fun getDistanceMatrix(
        @Query("origins") origins: String,
        @Query("destinations") destinations: String,
        @Query("key") apiKey: String = BuildConfig.MAP_API_KEY
    ): Call<DistanceMatrixDto>
}