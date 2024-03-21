package com.example.final_project.domain.model.order

data class GetOrder(
    val foodId: String,
    val restaurantId: Int,
    val name: String,
    val image: String,
    val menuCategory: String,
    val quantity: Int,
    val price: Double
)
