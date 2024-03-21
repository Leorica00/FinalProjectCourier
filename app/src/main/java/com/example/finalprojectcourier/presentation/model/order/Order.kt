package com.example.finalprojectcourier.presentation.model.order

data class Order(
    val foodId: String,
    val restaurantId: Int,
    val name: String,
    val image: String,
    val menuCategory: String,
    val quantity: Int,
    val price: Double
)