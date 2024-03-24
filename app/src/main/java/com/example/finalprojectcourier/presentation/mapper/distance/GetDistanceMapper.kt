package com.example.finalprojectcourier.presentation.mapper.distance

import com.example.finalprojectcourier.domain.model.distance.GetDistance
import com.example.finalprojectcourier.presentation.model.distance.Distance

fun GetDistance.toPresentation() = Distance(
    distance = distance,
    duration = duration,
    durationValue = durationValue,
    distanceValue = distanceValue
)