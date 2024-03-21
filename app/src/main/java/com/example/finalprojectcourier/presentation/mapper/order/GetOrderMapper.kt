package com.example.finalprojectcourier.presentation.mapper.order

import com.example.final_project.domain.model.order.GetOrder
import com.example.finalprojectcourier.presentation.model.order.Order

fun GetOrder.toPresentation() : Order {
    return Order(
        foodId = foodId,
        restaurantId = restaurantId,
        name = name,
        image = image,
        menuCategory = menuCategory,
        quantity = quantity,
        price = price
    )
}