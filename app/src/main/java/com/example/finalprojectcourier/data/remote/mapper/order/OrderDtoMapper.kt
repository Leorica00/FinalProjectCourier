package com.example.finalprojectcourier.data.remote.mapper.order

import com.example.final_project.domain.model.order.GetOrder
import com.example.finalprojectcourier.data.remote.model.order.OrderDto

fun GetOrder.toDto() = OrderDto(
    name = name,
    category = menuCategory,
    quantity = quantity
)

fun OrderDto.toDomain() = GetOrder(
    name = name ?: "",
    menuCategory = category ?: "",
    quantity = quantity ?: 0,
    foodId = "",
    restaurantId = 0,
    image = "",
    price = 0.0
)