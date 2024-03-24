package com.example.finalprojectcourier.presentation.event.delivery

sealed class CourierDeliveryMapEvent {
    object GetMenuUpdateEvent: CourierDeliveryMapEvent()
    object NavigateToChat: CourierDeliveryMapEvent()
    object UpdateCourierLocationEvent: CourierDeliveryMapEvent()
}
