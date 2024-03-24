package com.example.finalprojectcourier.presentation.screen.delivery_map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.final_project.data.remote.common.Resource
import com.example.final_project.presentation.mapper.chat.toDomain
import com.example.final_project.presentation.model.chat.Contact
import com.example.finalprojectcourier.domain.usecase.route.GetDirectionUseCase
import com.example.finalprojectcourier.presentation.state.CourierDeliveryState
import com.example.finalprojectcourier.domain.usecase.chat.AddContactUseCase
import com.example.finalprojectcourier.domain.usecase.order.GetOrderUseCase
import com.example.finalprojectcourier.presentation.event.delivery_map.ChatDeliveryEvents
import com.example.finalprojectcourier.presentation.mapper.order.toPresentation
import com.example.finalprojectcourier.presentation.mapper.toPresentation
import com.example.finalprojectcourier.presentation.util.getErrorMessage
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourierDeliveryMapViewModel @Inject constructor(
    private val getDirectionUseCase: GetDirectionUseCase,
    private val getOrderUseCase: GetOrderUseCase,
    private val addContactUseCase: AddContactUseCase
) : ViewModel() {
    private val _directionsStateFlow = MutableStateFlow(CourierDeliveryState())
    val directionStateFlow = _directionsStateFlow.asStateFlow()

    private val _uiEvent = MutableSharedFlow<DeliveryMapUiEvents>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun onEvent(event: DeliveryMapUiEvents) {
        when (event) {
            is DeliveryMapUiEvents.GoToChatFragment -> navigateToChatFragment()
        }
    }

    init {
        getLocationUpdate()
    }

    private fun navigateToChatFragment() {
        viewModelScope.launch {
            _uiEvent.emit(DeliveryMapUiEvents.GoToChatFragment)
        }
    }

    private fun getDirection(origin: LatLng, destination: LatLng) {
        viewModelScope.launch {
            getDirectionUseCase(origin = origin, destination = destination).collect {resource ->
                when(resource) {
                    is Resource.Loading -> {}
                    is Resource.Error -> updateErrorMessage(getErrorMessage(resource.error))
                    is Resource.Success -> _directionsStateFlow.update { currentState -> currentState.copy(direction = resource.response.toPresentation()) }
                }
            }
        }
    }

    private fun updateErrorMessage(errorMessage: Int?) {
        _directionsStateFlow.update { currentState -> currentState.copy(errorMessage = errorMessage) }
    }

    fun getMenuUpdate() {
        viewModelScope.launch {
            getOrderUseCase().collect {resource ->
                when(resource) {
                    is Resource.Success -> {
                        _directionsStateFlow.update { currentState -> currentState.copy(order = resource.response.toPresentation()) }
                        _directionsStateFlow.value.order?.let {
                            val contact = Contact(imageUrl = "", lastMessage = "", receiverId = it.userUuid, fullName = "user")
                            addContactUseCase(contact.toDomain())
                        }
                    }
                    is Resource.Error -> updateErrorMessage(getErrorMessage(resource.error))
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

sealed interface DeliveryMapUiEvents {
    object GoToChatFragment : DeliveryMapUiEvents
}