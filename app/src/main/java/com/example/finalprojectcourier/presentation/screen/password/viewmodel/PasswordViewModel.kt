package com.example.finalprojectcourier.presentation.screen.password.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.final_project.data.remote.common.Resource
import com.example.finalprojectcourier.domain.usecase.signup.SignInWithEmailPasswordUseCase
import com.example.final_project.presentation.event.EmailPasswordEvents
import com.example.final_project.presentation.state.LoginState
import com.example.finalprojectcourier.presentation.util.getErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordViewModel @Inject constructor(private val signInWithEmailPasswordUseCase: SignInWithEmailPasswordUseCase): ViewModel() {
    private val _logInState = MutableStateFlow(LoginState())
    val logInState: SharedFlow<LoginState> = _logInState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<EmailPasswordUiEvents>()
    val uiEvent: SharedFlow<EmailPasswordUiEvents> get() = _uiEvent

    fun onEvent(event: EmailPasswordEvents) {
        when (event) {
            is EmailPasswordEvents.SignInUsingEmailAndPassword -> logIn(event.email, event.password)
        }
    }

    private fun logIn(email: String, password: String) {
        viewModelScope.launch {
            signInWithEmailPasswordUseCase(email, password).collect {
                when (it) {
                    is Resource.Loading -> _logInState.update {
                            currentState -> currentState.copy(isLoading = true)
                    }

                    is Resource.Success -> {
                        _logInState.update { currentState -> currentState.copy(isLoading = false)}
                        _uiEvent.emit(EmailPasswordUiEvents.NavigateToHomeFragment)
                    }

                    is Resource.Error -> updateErrorMessage(getErrorMessage(it.error))
                }
            }
        }
    }

    private fun updateErrorMessage(errorMessage: Int?) {
        _logInState.update { currentState ->
            currentState.copy(errorMessage = errorMessage, isLoading = false)
        }
    }
}

sealed interface EmailPasswordUiEvents {
    object NavigateToHomeFragment : EmailPasswordUiEvents
}