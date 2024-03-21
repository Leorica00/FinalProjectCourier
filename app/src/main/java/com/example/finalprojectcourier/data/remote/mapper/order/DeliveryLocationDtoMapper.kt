package com.example.finalprojectcourier.data.remote.mapper.order

import com.example.finalprojectcourier.data.remote.model.order.DeliveryLocationDto
import com.example.finalprojectcourier.domain.model.order.GetDeliveryLocation
import com.google.android.gms.maps.model.LatLng

fun GetDeliveryLocation.toDto() = DeliveryLocationDto(
    latitude = location.latitude,
    longitude = location.longitude,
    locationName = locationName,
    locationType = locationType.name,
    entrance = entrance,
    floor = floor,
    apartmentNumber = apartmentNumber,
    extraDescription = extraDescription
)

fun DeliveryLocationDto.toDomain() = GetDeliveryLocation(
    location = LatLng(latitude!!, longitude!!),
    locationName = locationName!!,
    locationType = GetDeliveryLocation.GetLocationType.HOUSE,
    entrance = entrance!!,
    floor = floor!!,
    apartmentNumber = apartmentNumber!!,
    extraDescription = extraDescription,
    addressType = GetDeliveryLocation.GetAddressType.HOME
)