package com.example.finalprojectcourier.data.remote.model.order

data class OrderDto(
    val name: String?,
    val category: String?,
    val quantity: Int?
){
    constructor(): this(null, null, null)
}