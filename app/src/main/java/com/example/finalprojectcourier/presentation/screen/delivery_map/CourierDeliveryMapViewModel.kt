package com.example.finalprojectcourier.presentation.screen.delivery_map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.final_project.data.remote.common.Resource
import com.example.final_project.domain.usecase.distance.GetDistanceAndDurationUseCase
import com.example.finalprojectcourier.domain.usecase.location.GetCourierLocationUpdateUseCase
import com.example.final_project.presentation.mapper.chat.toDomain
import com.example.finalprojectcourier.presentation.mapper.location.toPresentation
import com.example.final_project.presentation.model.chat.Contact
import com.example.finalprojectcourier.domain.usecase.route.GetDirectionUseCase
import com.example.finalprojectcourier.presentation.state.CourierDeliveryState
import com.example.finalprojectcourier.domain.usecase.chat.AddContactUseCase
import com.example.finalprojectcourier.domain.usecase.location.UpdateCourierLocationUseCase
import com.example.finalprojectcourier.domain.usecase.order.GetOrderUseCase
import com.example.finalprojectcourier.domain.usecase.order.UpdateOrderUseCase
import com.example.finalprojectcourier.presentation.event.delivery.CourierDeliveryMapEvent
import com.example.finalprojectcourier.presentation.mapper.distance.toPresentation
import com.example.finalprojectcourier.presentation.mapper.location.toDomain
import com.example.finalprojectcourier.presentation.mapper.order.toDomain
import com.example.finalprojectcourier.presentation.mapper.order.toPresentation
import com.example.finalprojectcourier.presentation.mapper.toPresentation
import com.example.finalprojectcourier.presentation.util.getErrorMessage
import com.google.android.gms.maps.model.LatLng
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
    private val addContactUseCase: AddContactUseCase,
    private val getDistanceAndDurationUseCase: GetDistanceAndDurationUseCase,
    private val getCourierLocationUpdateUseCase: GetCourierLocationUpdateUseCase,
    private val updateCourierLocationUseCase: UpdateCourierLocationUseCase,
    private val updateOrderUseCase: UpdateOrderUseCase
) : ViewModel() {
    private val _directionsStateFlow = MutableStateFlow(CourierDeliveryState())
    val directionStateFlow = _directionsStateFlow.asStateFlow()

    private val _uiEvent = MutableSharedFlow<DeliveryMapUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun onEvent(event: CourierDeliveryMapEvent) {
        when(event) {
            is CourierDeliveryMapEvent.GetMenuUpdateEvent -> getMenuUpdate()
            is CourierDeliveryMapEvent.NavigateToChat -> navigateToChat()
            is CourierDeliveryMapEvent.UpdateCourierLocationEvent -> updateCourierLocation()
        }
    }

    init {
        getLocationUpdate()
    }

    private fun navigateToChat() {
        viewModelScope.launch {
            _uiEvent.emit(DeliveryMapUiEvent.GoToChatFragment)
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

    private fun updateDistanceAndDuration(destination: LatLng, origin: LatLng) {
        viewModelScope.launch {
            getDistanceAndDurationUseCase(destination, origin).collect { resource ->
                when (resource) {
                    is Resource.Success -> _directionsStateFlow.update { currentState ->
                        currentState.copy(distance = resource.response.toPresentation()) }

                    is Resource.Error -> updateErrorMessage(getErrorMessage(resource.error))
                    else -> {}
                }
            }
        }
    }

    private fun updateErrorMessage(errorMessage: Int?) {
        _directionsStateFlow.update { currentState -> currentState.copy(errorMessage = errorMessage) }
    }

    private fun getMenuUpdate() {
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
        viewModelScope.launch {
            getCourierLocationUpdateUseCase().collect {resource ->
                when(resource) {
                    is Resource.Success -> {
                        _directionsStateFlow.value.order?.let {
                            _directionsStateFlow.update { currentState -> currentState.copy(currentLocation = resource.response.toPresentation()) }
                            updateDistanceAndDuration(it.location!!.location, LatLng(resource.response.latitude, resource.response.longitude))
                            getDirection(origin = it.location.location, LatLng(resource.response.latitude, resource.response.longitude))
                        }
                    }
                    is Resource.Error -> updateErrorMessage(getErrorMessage(resource.error))
                    else -> {}
                }
            }
        }
    }

    private fun updateCourierLocation() {
        viewModelScope.launch {
            _directionsStateFlow.value.currentLocation?.let {
                updateCourierLocationUseCase(it.copy(isActive = false).toDomain())
                _directionsStateFlow.value.order?.let {
                    updateOrderUseCase(it.toDomain())
                }
            }
        }
    }
}

sealed interface DeliveryMapUiEvent {
    object GoToChatFragment : DeliveryMapUiEvent
}