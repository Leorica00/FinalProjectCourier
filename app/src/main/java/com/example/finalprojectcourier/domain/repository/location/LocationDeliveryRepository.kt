package com.example.finalprojectcourier.domain.repository.location

import com.example.final_project.data.remote.common.Resource
import com.example.finalprojectcourier.domain.model.location.GetLocationDelivery
import kotlinx.coroutines.flow.Flow

interface LocationDeliveryRepository {
    suspend fun getCourierLocation(): Flow<Resource<GetLocationDelivery>>
    suspend fun updateCourierLocation(location: GetLocationDelivery)
}