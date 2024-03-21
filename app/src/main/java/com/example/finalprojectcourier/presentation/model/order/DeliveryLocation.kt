package com.example.finalprojectcourier.presentation.model.order

import com.example.final_project.presentation.model.delivery_location.AddressType
import com.example.final_project.presentation.model.delivery_location.LocationType
import com.google.android.gms.maps.model.LatLng

data class DeliveryLocation(
    val location: LatLng,
    val locationName: String,
    val locationType: LocationType,
    val addressType: AddressType,
    val entrance: Int,
    val floor: Int,
    val apartmentNumber: Int,
    val extraDescription: String? = null
)

