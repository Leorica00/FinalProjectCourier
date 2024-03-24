package com.example.finalprojectcourier.presentation.mapper.order

import com.example.final_project.presentation.model.delivery_location.AddressType
import com.example.final_project.presentation.model.delivery_location.LocationType
import com.example.finalprojectcourier.R
import com.example.finalprojectcourier.domain.model.order.GetDeliveryLocation
import com.example.finalprojectcourier.domain.model.order.GetDeliveryLocation.GetAddressType
import com.example.finalprojectcourier.domain.model.order.GetDeliveryLocation.GetLocationType
import com.example.finalprojectcourier.presentation.model.order.DeliveryLocation

fun DeliveryLocation.toDomain() = GetDeliveryLocation(
    location = location,
    locationName = locationName,
    locationType = locationTypeConvertor(locationType),
    addressType = addressTypeConvertor(addressType),
    entrance = entrance,
    floor = floor,
    apartmentNumber = apartmentNumber,
    extraDescription = extraDescription,
)

private fun locationTypeConvertor(locationType: LocationType): GetLocationType {
    return when(locationType.text) {
        R.string.house -> GetLocationType.HOUSE
        R.string.office -> GetLocationType.OFFICE
        R.string.apartment -> GetLocationType.APARTMENT
        R.string.other -> GetLocationType.OTHER
        else -> GetLocationType.APARTMENT
    }
}

private fun addressTypeConvertor(addressType: AddressType): GetAddressType {
    return when(addressType.text) {
        R.string.home_home -> GetAddressType.HOME
        R.string.work -> GetAddressType.WORK
        R.string.other -> GetAddressType.OTHER
        else -> GetAddressType.HOME
    }
}