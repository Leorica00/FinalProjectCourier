package com.example.finalprojectcourier.data.remote.model.order

data class SubmitOrderDto(
    val userUuid: String?,
    val active: Boolean?,
    val location: DeliveryLocationDto?,
    val menu: List<OrderDto>?,
    val totalPrice: Double?
)
{
    constructor() : this(null, null, null, null, null)
}