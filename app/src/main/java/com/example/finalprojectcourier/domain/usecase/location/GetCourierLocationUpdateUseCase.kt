package com.example.finalprojectcourier.domain.usecase.location

import com.example.finalprojectcourier.domain.repository.location.LocationDeliveryRepository
import javax.inject.Inject

class GetCourierLocationUpdateUseCase @Inject constructor(private val locationDeliveryRepository: LocationDeliveryRepository) {
    suspend operator fun invoke() = locationDeliveryRepository.getCourierLocation()
}