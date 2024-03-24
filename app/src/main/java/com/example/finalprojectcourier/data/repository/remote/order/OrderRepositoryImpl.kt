package com.example.finalprojectcourier.data.repository.remote.order

import com.example.final_project.data.remote.common.HandleErrorStates
import com.example.final_project.data.remote.common.Resource
import com.example.finalprojectcourier.data.remote.mapper.order.toData
import com.example.finalprojectcourier.data.remote.mapper.order.toDomain
import com.example.finalprojectcourier.data.remote.model.order.SubmitOrderDto
import com.example.finalprojectcourier.domain.model.order.GetSubmitOrder
import com.example.finalprojectcourier.domain.repository.order.OrderRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val databaseReference: DatabaseReference,
): OrderRepository {
    override suspend fun getOrder(): Flow<Resource<GetSubmitOrder>> {
        return callbackFlow {
            trySend(Resource.Loading(loading = true))

            databaseReference.child("orders").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val order = mutableListOf<SubmitOrderDto>()
                    snapshot.children.forEach {
                        order.add(it.getValue(SubmitOrderDto::class.java)!!)
                    }

                    trySend(Resource.Success(response = order.map { it.toDomain() }.first()))
                }

                override fun onCancelled(error: DatabaseError) {}
            })

            trySend(Resource.Loading(loading = false))
            awaitClose {}
        }.catch {
            emit(Resource.Error(HandleErrorStates.handleException(it as Exception), throwable = it))
        }.flowOn(IO)
    }

    override suspend fun updateOrder(order: GetSubmitOrder): Unit = withContext(IO) {
        val orderRef = databaseReference.child("orders").child("0")
        orderRef.setValue(order.toData().copy(active = false)).await()
    }
}