package com.example.finalprojectcourier.presentation.mapper.order

import com.example.finalprojectcourier.domain.model.order.GetSubmitOrder
import com.example.finalprojectcourier.presentation.model.order.SubmitOrder

fun GetSubmitOrder.toPresentation() = SubmitOrder(
    isActive = isActive,
    location = location?.toPresentation(),
    menu = menu?.map { it.toPresentation() },
    totalPrice = totalPrice,
    userUuid = userUuid,
    fullName = fullName
)