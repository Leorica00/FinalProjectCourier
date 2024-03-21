package com.example.finalprojectcourier.domain.usecase.order

import com.example.finalprojectcourier.domain.repository.order.OrderRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetOrderUseCase @Inject constructor(private val orderRepository: OrderRepository) {
    suspend operator fun invoke() = orderRepository.getOrder().flowOn(IO)
}