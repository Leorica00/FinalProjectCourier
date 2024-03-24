package com.example.finalprojectcourier.domain.usecase.order

import com.example.finalprojectcourier.domain.model.order.GetSubmitOrder
import com.example.finalprojectcourier.domain.repository.order.OrderRepository
import javax.inject.Inject

class UpdateOrderUseCase @Inject constructor(private val orderRepository: OrderRepository) {
    suspend operator fun invoke(order: GetSubmitOrder) = orderRepository.updateOrder(order)
}