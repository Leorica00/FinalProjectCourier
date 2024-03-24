package com.example.finalprojectcourier.data.remote.model.location

data class LocationDeliveryDto(
    val isActive: Boolean?,
    val latitude: Double?,
    val longitude: Double?
) {
    constructor(): this(null, null, null)
}
