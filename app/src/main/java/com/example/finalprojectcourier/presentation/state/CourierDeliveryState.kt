package com.example.finalprojectcourier.presentation.state

import com.example.finalprojectcourier.presentation.model.Direction
import com.example.finalprojectcourier.presentation.model.order.SubmitOrder
import com.google.android.gms.maps.model.LatLng

data class CourierDeliveryState(
    val direction: Direction? = null,
    val order: SubmitOrder? = null,
    val userLatLng: LatLng? = null,
    val errorMessage: Int? = null
)
