package com.example.finalprojectcourier.domain.usecase.route

import com.example.final_project.domain.repository.route.DirectionsRepository
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class GetDirectionUseCase @Inject constructor(private val directionsRepository: DirectionsRepository) {
    suspend operator fun invoke(origin: LatLng, destination: LatLng) = directionsRepository.getDirections(origin, destination)
}