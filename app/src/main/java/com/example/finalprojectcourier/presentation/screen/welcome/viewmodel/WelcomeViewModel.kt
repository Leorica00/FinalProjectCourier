package com.example.finalprojectcourier.presentation.screen.welcome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor() : ViewModel() {
    private val _navigationEvent = MutableSharedFlow<WelcomeUiEvent>()
    val navigationEvent: SharedFlow<WelcomeUiEvent> get() = _navigationEvent

    fun onUiEvent(event: WelcomeUiEvent) {
        when (event) {
            is WelcomeUiEvent.NavigateToLoginFragment -> {
                viewModelScope.launch {
                    _navigationEvent.emit(WelcomeUiEvent.NavigateToLoginFragment)
                }
            }
        }
    }
}

sealed class WelcomeUiEvent {
    object NavigateToLoginFragment : WelcomeUiEvent()
}