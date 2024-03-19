package com.example.finalprojectcourier.presentation.screen.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.final_project.data.remote.common.Resource
import com.example.finalprojectcourier.domain.usecase.signup.SendVerificationCodeUseCase
import com.example.finalprojectcourier.domain.usecase.validators.EmailValidationUseCase
import com.example.finalprojectcourier.domain.usecase.validators.PhoneNumberValidatorUseCase
import com.example.final_project.presentation.event.LoginEvent
import com.example.final_project.presentation.state.VerificationState
import com.example.finalprojectcourier.presentation.util.getErrorMessage
import com.example.finalprojectcourier.R
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
class LoginViewModel @Inject constructor(
    private val sendVerificationCodeUseCase: SendVerificationCodeUseCase,
    private val phoneNumberValidatorUseCase: PhoneNumberValidatorUseCase,
    private val emailValidationUseCase: EmailValidationUseCase,
) : ViewModel() {
    private val _navigationEvent = MutableSharedFlow<LoginFragmentUiEvents>()
    val navigationEvent: SharedFlow<LoginFragmentUiEvents> get() = _navigationEvent

    private val _verificationState = MutableStateFlow(VerificationState())
    val verificationState: StateFlow<VerificationState> get() = _verificationState

    fun onUiEvent(events: LoginFragmentUiEvents) {
        when (events) {
            is LoginFragmentUiEvents.NavigateToHomePage -> {
                viewModelScope.launch {
                    _navigationEvent.emit(LoginFragmentUiEvents.NavigateToHomePage)
                }
            }

            is LoginFragmentUiEvents.NavigateToSignUpPage -> {
                viewModelScope.launch {
                    _navigationEvent.emit(LoginFragmentUiEvents.NavigateToSignUpPage)
                }
            }

            is LoginFragmentUiEvents.NavigateToSmsAuthPage -> {
                viewModelScope.launch {
                    _navigationEvent.emit(LoginFragmentUiEvents.NavigateToSmsAuthPage(events.verificationId))
                }
            }

            is LoginFragmentUiEvents.ForgotPassword -> {
                viewModelScope.launch {
                    _navigationEvent.emit(LoginFragmentUiEvents.ForgotPassword)
                }
            }

            is LoginFragmentUiEvents.NavigateToEmailPasswordPage -> {
                viewModelScope.launch {
                    _navigationEvent.emit(LoginFragmentUiEvents.NavigateToEmailPasswordPage(events.email))
                }
            }
        }
    }

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.SignInUserWithCredential -> sendVerification(event.credential, event.options)
        }
    }

    private fun sendVerification(credential: String, options: PhoneAuthOptions.Builder) {
        viewModelScope.launch {
            if (phoneNumberValidatorUseCase(credential)) {
                sendVerificationCodeUseCase(credential, options).collect { resource ->
                    when (resource) {
                        is Resource.Loading -> _verificationState.update { currentState ->
                            currentState.copy(isLoading = true)
                        }

                        is Resource.Success -> {
                            _verificationState.update { currentState ->
                                currentState.copy(data = resource.response)
                            }
                            _navigationEvent.emit(
                                LoginFragmentUiEvents.NavigateToSmsAuthPage(
                                    resource.response
                                )
                            )
                        }

                        is Resource.Error -> updateErrorMessage(getErrorMessage(resource.error))
                    }
                }
            }

            if (emailValidationUseCase(credential)) {
                _navigationEvent.emit(LoginFragmentUiEvents.NavigateToEmailPasswordPage(email = credential))
            }
            validateErrorMessage(credential)
        }
    }

    private fun updateErrorMessage(errorMessage: Int?) {
        _verificationState.update { currentState ->
            currentState.copy(errorMessage = errorMessage, isLoading = false)
        }
    }

    private fun validatePhoneNumber(phoneNumber: String): Boolean {
        return phoneNumberValidatorUseCase(phoneNumber = phoneNumber)
    }

    private fun validateEmail(email: String): Boolean {
        return emailValidationUseCase(email)
    }

    private fun validateErrorMessage(credential: String) {
        if (!validateEmail(credential))
            if(!validatePhoneNumber(credential))
                updateErrorMessage(errorMessage = R.string.phone_email_validation_error)
    }
}

sealed class LoginFragmentUiEvents {
    object NavigateToHomePage : LoginFragmentUiEvents()
    object NavigateToSignUpPage : LoginFragmentUiEvents()
    object ForgotPassword : LoginFragmentUiEvents()
    data class NavigateToSmsAuthPage(val verificationId: String) : LoginFragmentUiEvents()
    data class NavigateToEmailPasswordPage(val email: String) : LoginFragmentUiEvents()
}