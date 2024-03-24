package com.example.final_project.data.remote.mapper.location

import com.example.finalprojectcourier.data.remote.model.location.LocationDeliveryDto
import com.example.finalprojectcourier.domain.model.location.GetLocationDelivery

fun LocationDeliveryDto.toDomain() = GetLocationDelivery(
    isActive = isActive ?: false,
    latitude = latitude ?: 0.0,
    longitude = longitude ?: 0.0
)