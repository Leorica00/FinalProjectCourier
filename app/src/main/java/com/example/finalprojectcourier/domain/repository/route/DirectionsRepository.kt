package com.example.final_project.domain.repository.route

import com.example.final_project.data.remote.common.Resource
import com.example.finalprojectcourier.domain.model.GetDirection
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow

interface DirectionsRepository {
    suspend fun getDirections(origin: LatLng, destination: LatLng): Flow<Resource<GetDirection>>
}