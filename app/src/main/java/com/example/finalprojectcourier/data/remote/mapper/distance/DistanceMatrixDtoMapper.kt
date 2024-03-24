package com.example.finalprojectcourier.data.remote.mapper.distance

import com.example.finalprojectcourier.data.remote.model.distance.DistanceMatrixDto
import com.example.finalprojectcourier.domain.model.distance.GetDistance

fun DistanceMatrixDto.toDomain() = GetDistance(
    distance = rows.firstOrNull()?.elements?.firstOrNull()?.distance?.text ?: "",
    duration = rows.firstOrNull()?.elements?.firstOrNull()?.duration?.text ?: "",
    distanceValue = rows.firstOrNull()?.elements?.firstOrNull()?.distance?.value ?: 0,
    durationValue = rows.firstOrNull()?.elements?.firstOrNull()?.duration?.value ?: 0
)