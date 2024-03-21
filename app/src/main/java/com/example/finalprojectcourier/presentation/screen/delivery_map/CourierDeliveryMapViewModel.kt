package com.example.finalprojectcourier.presentation.screen.delivery_map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.final_project.data.remote.common.Resource
import com.example.finalprojectcourier.domain.usecase.route.GetDirectionUseCase
import com.example.final_project.presentation.state.CourierDeliveryState
import com.example.finalprojectcourier.domain.usecase.order.GetOrderUseCase
import com.example.finalprojectcourier.presentation.mapper.order.toPresentation
import com.example.finalprojectcourier.presentation.mapper.toPresentation
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourierDeliveryMapViewModel @Inject constructor(
    private val getDirectionUseCase: GetDirectionUseCase,
    private val getOrderUseCase: GetOrderUseCase
) : ViewModel() {
    private val _directionsStateFlow = MutableStateFlow(CourierDeliveryState())
    val directionStateFlow = _directionsStateFlow.asStateFlow()

    init {
        getLocationUpdate()
    }

    fun getDirection(origin: LatLng, destination: LatLng) {
        viewModelScope.launch {
            getDirectionUseCase(origin = origin, destination = destination).collect {resource ->
                when(resource) {
                    is Resource.Loading -> {}
                    is Resource.Error -> {}
                    is Resource.Success -> _directionsStateFlow.update { currentState -> currentState.copy(direction = resource.response.toPresentation()) }
                }
            }
        }
    }

    fun getMenuUpdate() {
        viewModelScope.launch {
            getOrderUseCase().collect {resource ->
                when(resource) {
                    is Resource.Success -> _directionsStateFlow.update { currentState -> currentState.copy(order = resource.response.toPresentation()) }
                    else -> {}
                }
            }
        }
    }

    private fun getLocationUpdate() {
        Firebase.database.reference.child("deliveries").child("your_delivery_id")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val location = mutableMapOf<String?, Double?>()
                    snapshot.children.forEach {
                        location[it.key] = it.getValue(Double::class.java)
                    }

                    _directionsStateFlow.value.order?.let {
                        getDirection(origin = it.location!!.location, LatLng(location["latitude"]!!, location["longitude"]!!))
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }
}