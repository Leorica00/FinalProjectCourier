package com.example.finalprojectcourier.presentation.mapper.order

import com.example.finalprojectcourier.domain.model.order.GetSubmitOrder
import com.example.finalprojectcourier.presentation.model.order.SubmitOrder

fun SubmitOrder.toDomain() = GetSubmitOrder(
    isActive = isActive,
    location = location?.toDomain(),
    menu = menu!!.map { it.toDomain() },
    totalPrice = totalPrice,
    userUuid = userUuid,
    fullName = fullName
)