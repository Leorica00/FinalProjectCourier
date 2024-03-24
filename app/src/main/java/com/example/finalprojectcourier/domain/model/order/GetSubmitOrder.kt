package com.example.finalprojectcourier.domain.model.order

import com.example.final_project.domain.model.order.GetOrder

data class GetSubmitOrder(
    val userUuid: String?,
    val isActive: Boolean?,
    val location: GetDeliveryLocation?,
    val menu: List<GetOrder>?,
    val totalPrice: Double?
)