package com.example.finalprojectcourier.data.remote.mapper.location

import com.example.finalprojectcourier.data.remote.model.location.LocationDeliveryDto
import com.example.finalprojectcourier.domain.model.location.GetLocationDelivery

fun GetLocationDelivery.toData() = LocationDeliveryDto(
    isActive = isActive,
    latitude = latitude,
    longitude = longitude
)