package com.example.finalprojectcourier.domain.model.order

import com.google.android.gms.maps.model.LatLng

data class GetDeliveryLocation(
    val location: LatLng,
    val locationName: String,
    val locationType: GetLocationType,
    val addressType: GetAddressType,
    val entrance: Int,
    val floor: Int,
    val apartmentNumber: Int,
    val extraDescription: String? = null
) {
    enum class GetLocationType{
        APARTMENT,
        HOUSE,
        OFFICE,
        OTHER
    }

    enum class GetAddressType {
        HOME,
        WORK,
        OTHER
    }
}

