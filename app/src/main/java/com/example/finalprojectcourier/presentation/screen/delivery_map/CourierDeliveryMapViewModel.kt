package com.example.finalprojectcourier.presentation.screen.delivery_map

import android.util.Log.d
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.final_project.data.remote.common.Resource
import com.example.finalprojectcourier.domain.usecase.route.GetDirectionUseCase
import com.example.final_project.presentation.mapper.toPresentation
import com.example.final_project.presentation.state.CourierDeliveryState
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
class CourierDeliveryMapViewModel @Inject constructor(private val getDirectionUseCase: GetDirectionUseCase) : ViewModel() {
    private val _directionsStateFlow = MutableStateFlow(CourierDeliveryState())
    val directionStateFlow = _directionsStateFlow.asStateFlow()

    init {
        getLocationUpdate()
    }

    fun getDirection(origin: LatLng, destination: LatLng) {
        viewModelScope.launch {
            getDirectionUseCase(origin = origin, destination = destination).collect {resource ->
                d("ResourceResponse", resource.toString())
                when(resource) {
                    is Resource.Loading -> {}
                    is Resource.Error -> {}
                    is Resource.Success -> _directionsStateFlow.update { currentState -> currentState.copy(direction = resource.response.toPresentation()) }
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
                        location.put(it.key, it.getValue(Double::class.java))
                    }

                    d("locationUpdated", location.toString())
                    getDirection(origin = LatLng(41.709904512556946, 44.79725170393272), LatLng(location["latitude"]!!, location["longitude"]!!))
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }
}