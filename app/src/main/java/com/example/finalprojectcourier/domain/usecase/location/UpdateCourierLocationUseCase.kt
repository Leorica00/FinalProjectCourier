package com.example.finalprojectcourier.domain.usecase.location

import com.example.finalprojectcourier.domain.model.location.GetLocationDelivery
import com.example.finalprojectcourier.domain.repository.location.LocationDeliveryRepository
import javax.inject.Inject

class UpdateCourierLocationUseCase @Inject constructor(private val locationDeliveryRepository: LocationDeliveryRepository) {
    suspend operator fun invoke(location: GetLocationDelivery) = locationDeliveryRepository.updateCourierLocation(location)
}