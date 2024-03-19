package com.example.finalprojectcourier.presentation.screen.passcode.viewmodel

import android.util.Log.d
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.final_project.data.remote.common.HandleErrorStates
import com.example.final_project.data.remote.common.Resource
import com.example.finalprojectcourier.domain.usecase.signup.SignInWithAuthCredentialUseCase
import com.example.final_project.presentation.event.PasscodeEvent
import com.example.final_project.presentation.model.Passcode
import com.example.final_project.presentation.state.AuthState
import com.example.final_project.presentation.state.PasscodeState
import com.example.finalprojectcourier.presentation.util.getErrorMessage
import com.example.finalprojectcourier.R
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasscodeViewModel @Inject constructor(
    private val signInWithAuthCredentialUseCase: SignInWithAuthCredentialUseCase
) : ViewModel() {
    private val _passcodeStateFlow = MutableStateFlow(PasscodeState())
    val passcodeStateFlow = _passcodeStateFlow.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<PasscodeNavigationEvents>()
    val navigationEvent: SharedFlow<PasscodeNavigationEvents> get() = _navigationEvent

    private val _authStateFlow = MutableStateFlow(AuthState())
    val authStateFlow = _authStateFlow.asStateFlow()

    fun onEvent(event: PasscodeEvent) {
        when(event) {
            is PasscodeEvent.ChangeTextInputEvent -> changeTextInput(passcode = event.passcode)
            is PasscodeEvent.ResetPasscode -> resetPasscode()
            is PasscodeEvent.SignInWithVerificationCode -> signInWithVerificationCode(
                verificationId = event.verificationId
            )
            is PasscodeEvent.SignUp -> signUp(event.verificationId)
        }
    }

    fun onUiEvent(events: PasscodeNavigationEvents) {
        when (events) {
            is PasscodeNavigationEvents.NavigateBack -> {
                viewModelScope.launch {
                    _navigationEvent.emit(PasscodeNavigationEvents.NavigateBack)
                }
            }

            is PasscodeNavigationEvents.NavigateToSignUpCredentialsPage -> {
                viewModelScope.launch {
                    _navigationEvent.emit(
                        PasscodeNavigationEvents.NavigateToSignUpCredentialsPage(
                            events.phoneNumber
                        )
                    )
                }
            }

            is PasscodeNavigationEvents.NavigateToHomePage -> {
                viewModelScope.launch {
                    _navigationEvent.emit(PasscodeNavigationEvents.NavigateToHomePage)
                }
            }
        }
    }

    private fun signUp(verificationId: String) {
        viewModelScope.launch {
            val smsCode = _passcodeStateFlow.value.passcode
                .mapNotNull { it.currentNumber?.toString() }
                .reduceOrNull { acc, s -> acc + s }

            smsCode?.let {
                val credential = PhoneAuthProvider.getCredential(verificationId, it)
                signInWithAuthCredentialUseCase(credential).collect {resource ->
                    d("resourceSignuper", resource.toString())

                    when (resource) {
                        is Resource.Loading -> _authStateFlow.update { currentState ->
                            currentState.copy(isLoading = true)
                        }

                        is Resource.Success -> {
                            _authStateFlow.update { currentState ->
                                currentState.copy(data = resource.response)
                            }

                            _navigationEvent.emit(
                                PasscodeNavigationEvents.NavigateToSignUpCredentialsPage(
                                    phoneNumber = resource.response.user?.phoneNumber
                                )
                            )
                        }

                        is Resource.Error -> {
                            updateErrorMessage(getErrorMessage(resource.error))
                            _passcodeStateFlow.update { currentState -> currentState.copy(errorMessage = getErrorMessage(resource.error)) }
                        }
                    }
                }
            }
        }
    }

    private fun signInWithVerificationCode(verificationId: String) {
        viewModelScope.launch {
            val smsCode = _passcodeStateFlow.value.passcode
                .mapNotNull { it.currentNumber?.toString() }
                .reduceOrNull { acc, s -> acc + s }

            val credential = PhoneAuthProvider.getCredential(verificationId, smsCode!!)


            signInWithAuthCredentialUseCase(credential).collect {resource ->
                when (resource) {
                    is Resource.Loading -> _authStateFlow.update { currentState ->
                        currentState.copy(isLoading = true)
                    }

                    is Resource.Success -> {
                        _authStateFlow.update { currentState ->
                            currentState.copy(data = resource.response)
                        }

                        _navigationEvent.emit(
                            PasscodeNavigationEvents.NavigateToHomePage
                        )
                    }

                    is Resource.Error -> {
                        if (resource.error.errorCode == HandleErrorStates.ErrorCode.INVALID_CREDENTIALS)
                            updateErrorMessage(R.string.invalid_passcode_error)
                        else
                            updateErrorMessage(getErrorMessage(resource.error))
                    }
                }
            }
        }
    }

    private fun updateErrorMessage(errorMessage: Int?) {
        _authStateFlow.update { currentState ->
            currentState.copy(errorMessage = errorMessage, isLoading = false)
        }
    }

    private fun resetPasscode() {
        _passcodeStateFlow.update { currentState -> currentState.copy(passcode = (1..6).map { Passcode(it) }) }
    }

    private fun changeTextInput(passcode: Passcode) {
        _passcodeStateFlow.update { currentState -> currentState.copy(errorMessage = null) }
        val newPasscode = _passcodeStateFlow.value.passcode.map {
            if (it.id == passcode.id) {
                it.copy(currentNumber = passcode.currentNumber)
            }else {
                it
            }}

        _passcodeStateFlow.update { currentState -> currentState.copy(passcode = newPasscode) }

        if (passcode.id == 6) {
            _passcodeStateFlow.update { currentState -> currentState.copy(successMessage = true) }
        }
    }
}

sealed class PasscodeNavigationEvents {
    object NavigateBack : PasscodeNavigationEvents()
    object NavigateToHomePage : PasscodeNavigationEvents()
    data class NavigateToSignUpCredentialsPage(val phoneNumber: String?) : PasscodeNavigationEvents()
}