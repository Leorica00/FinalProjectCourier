package com.example.finalprojectcourier.presentation.screen.signup.start.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.final_project.data.remote.common.Resource
import com.example.finalprojectcourier.domain.usecase.signup.SendVerificationCodeUseCase
import com.example.finalprojectcourier.domain.usecase.validators.PhoneNumberValidatorUseCase
import com.example.final_project.presentation.event.signup.SendSmsEvent
import com.example.final_project.presentation.state.VerificationState
import com.example.finalprojectcourier.presentation.util.getErrorMessage
import com.example.finalprojectcourier.R
import com.example.finalprojectcourier.presentation.model.ErrorType
import com.google.firebase.auth.PhoneAuthOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val sendVerificationCodeUseCase: SendVerificationCodeUseCase,
    private val phoneNumberValidatorUseCase: PhoneNumberValidatorUseCase
) : ViewModel() {

    private val _navigationEvent = MutableSharedFlow<SignUpNavigationEvents>()
    val navigationEvent: SharedFlow<SignUpNavigationEvents> get() = _navigationEvent

    private val _verificationState = MutableStateFlow(VerificationState())
    val verificationState: StateFlow<VerificationState> get() = _verificationState

    fun onUiEvent(events: SignUpNavigationEvents) {
        when (events) {
            is SignUpNavigationEvents.NavigateToSignInPage -> {
                viewModelScope.launch {
                    _navigationEvent.emit(SignUpNavigationEvents.NavigateToSignInPage)
                }
            }

            is SignUpNavigationEvents.NavigateToOtpFillPage -> {
                viewModelScope.launch {
                    _navigationEvent.emit(
                        SignUpNavigationEvents.NavigateToOtpFillPage(
                            events.phoneNumber,
                            events.verificationId
                        )
                    )
                }
            }
        }
    }

    fun onEvent(event: SendSmsEvent) {
        when (event) {
            is SendSmsEvent.SendSmsToProvidedNumber -> sendVerificationCodeToPhoneNumber(event.phoneNumber, event.options)
        }
    }

    private fun sendVerificationCodeToPhoneNumber(phoneNumber: String, options: PhoneAuthOptions.Builder) {
        viewModelScope.launch {
            if (validatePhoneNumber(phoneNumber)) {
                 sendVerificationCodeUseCase(phoneNumber, options).collect { resource ->
                    when (resource) {
                        is Resource.Loading -> _verificationState.update { currentState ->
                            currentState.copy(isLoading = true)
                        }

                        is Resource.Success -> {
                            _verificationState.update { currentState ->

                                currentState.copy(data = resource.response)
                            }

                            _navigationEvent.emit(
                                SignUpNavigationEvents.NavigateToOtpFillPage(
                                    phoneNumber = phoneNumber,
                                    verificationId = resource.response
                                )
                            )
                        }

                        is Resource.Error -> updateErrorMessage(getErrorMessage(resource.error), ErrorType.GENERAL)
                    }
                }
            }
        }
    }

    private fun validatePhoneNumber(phoneNumber: String): Boolean {
        return if (!phoneNumberValidatorUseCase(phoneNumber)) {
            updateErrorMessage(R.string.phone_validation_error, ErrorType.PHONE)
            false
        }else
            true
    }

    private fun updateErrorMessage(errorMessage: Int?, errorType: ErrorType) {
        when(errorType) {
            ErrorType.All -> _verificationState.update { currentState -> currentState.copy(phoneErrorMessage = errorMessage, errorMessage = errorMessage, isLoading = false) }
            ErrorType.PHONE -> _verificationState.update { currentState -> currentState.copy(phoneErrorMessage = errorMessage, isLoading = false) }
            else -> _verificationState.update { currentState ->
                currentState.copy(errorMessage = errorMessage, isLoading = false)
            }
        }
    }
}

sealed class SignUpNavigationEvents {
    data object NavigateToSignInPage : SignUpNavigationEvents()
    data class NavigateToOtpFillPage(val phoneNumber: String, val verificationId: String) : SignUpNavigationEvents()
}