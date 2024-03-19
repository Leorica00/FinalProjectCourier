package com.example.finalprojectcourier.data.remote.service

import com.example.finalprojectcourier.BuildConfig
import com.example.finalprojectcourier.data.remote.model.DirectionsResponseDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DirectionsApiService {

    @GET("/maps/api/directions/json")
    fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") apiKey: String = BuildConfig.MAP_API_KEY
    ): Call<DirectionsResponseDto>
}