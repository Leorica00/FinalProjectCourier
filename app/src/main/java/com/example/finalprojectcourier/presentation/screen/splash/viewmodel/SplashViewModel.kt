package com.example.finalprojectcourier.presentation.screen.splash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalprojectcourier.domain.usecase.chatbot.UpdateAuthTokenUseCase
import com.example.finalprojectcourier.domain.usecase.firebase.GetAuthStateUseCase
import com.example.final_project.presentation.event.splash.SplashNavigationEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getAuthStateUseCase: GetAuthStateUseCase,
    private val updateAuthTokenUseCase: UpdateAuthTokenUseCase,
) : ViewModel() {

//    private val _settingsStateFlow = MutableStateFlow(SettingsState())
//    val settingsStateFlow = _settingsStateFlow.asStateFlow()

    private val _uiEvent = MutableStateFlow<SplashNavigationEvents>(SplashNavigationEvents.Pending)
    val uiEvent: StateFlow<SplashNavigationEvents> get() = _uiEvent

    init {
//        readDarkModeState()
//        readLanguageState()
        readSession()
    }

//    private fun readDarkModeState() {
//        viewModelScope.launch {
//            readDarkModeUseCase().collect {
//                _settingsStateFlow.update { currentState -> currentState.copy(isDarkMode = it) }
//            }
//        }
//    }
//
//    private fun readLanguageState() {
//        viewModelScope.launch {
//            readLanguageUseCase().collect {
//                _settingsStateFlow.update { currentState -> currentState.copy(language = it) }
//            }
//        }
//    }

    private fun readSession() {
        viewModelScope.launch {
            updateAuthTokenUseCase()
            getAuthStateUseCase().collect {
                if (it)
                    _uiEvent.emit(SplashNavigationEvents.NavigateToHome)
                else
                    _uiEvent.emit(SplashNavigationEvents.NavigateToLogIn)
            }
        }
    }
}