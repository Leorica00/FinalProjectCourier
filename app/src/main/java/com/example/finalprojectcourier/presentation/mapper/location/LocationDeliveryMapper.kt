package com.example.finalprojectcourier.presentation.mapper.location

import com.example.finalprojectcourier.domain.model.location.GetLocationDelivery
import com.example.finalprojectcourier.presentation.model.location.LocationDelivery

fun GetLocationDelivery.toPresentation() = LocationDelivery(
    isActive = isActive,
    latitude = latitude,
    longitude = longitude
)