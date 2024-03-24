package com.example.final_project.domain.usecase.distance

import com.example.finalprojectcourier.domain.repository.distance.DistanceRepository
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class GetDistanceAndDurationUseCase @Inject constructor(private val distanceRepository: DistanceRepository) {
    suspend operator fun invoke(origin: LatLng, destination: LatLng) = distanceRepository.getDistanceAndDuration(origin, destination)
}