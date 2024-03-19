package com.example.finalprojectcourier.presentation.screen.signup.success.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpSuccessViewModel @Inject constructor(): ViewModel() {
    private val _navigationEvent = MutableSharedFlow<SignUpSuccessNavigationEvents>()
    val navigationEvent: SharedFlow<SignUpSuccessNavigationEvents> get() = _navigationEvent

    fun onUiEvent(events: SignUpSuccessNavigationEvents) {
        when (events) {
            is SignUpSuccessNavigationEvents.NavigateToHomePage -> {
                viewModelScope.launch {
                    _navigationEvent.emit(SignUpSuccessNavigationEvents.NavigateToHomePage)
                }
            }
        }
    }
}

sealed class SignUpSuccessNavigationEvents {
    object NavigateToHomePage : SignUpSuccessNavigationEvents()
}