package com.example.finalprojectcourier.data.remote.mapper.order

import com.example.finalprojectcourier.data.remote.model.order.SubmitOrderDto
import com.example.finalprojectcourier.domain.model.order.GetSubmitOrder

fun GetSubmitOrder.toData() = SubmitOrderDto(
    active = isActive,
    location = location?.toDto(),
    totalPrice = totalPrice,
    menu = menu?.map { it.toDto() },
    userUuid = userUuid,
    fullName = fullName
)

fun SubmitOrderDto.toDomain() = GetSubmitOrder(
    isActive = active,
    location = location?.toDomain(),
    totalPrice = totalPrice,
    menu = menu?.map { it.toDomain() },
    userUuid = userUuid,
    fullName = fullName
)