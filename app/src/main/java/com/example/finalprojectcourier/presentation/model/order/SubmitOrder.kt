package com.example.finalprojectcourier.presentation.model.order

data class SubmitOrder(
    val userUuid: String?,
    val isActive: Boolean?,
    val location: DeliveryLocation?,
    val menu: List<Order>?,
    val totalPrice: Double?
){
    constructor() : this(null, null, null, null, null)
}