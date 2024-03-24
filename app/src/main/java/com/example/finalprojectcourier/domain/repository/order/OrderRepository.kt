package com.example.finalprojectcourier.domain.repository.order

import com.example.final_project.data.remote.common.Resource
import com.example.finalprojectcourier.domain.model.order.GetSubmitOrder
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    suspend fun getOrder(): Flow<Resource<GetSubmitOrder>>
    suspend fun updateOrder(order: GetSubmitOrder)
}