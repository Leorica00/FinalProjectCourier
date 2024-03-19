package com.example.final_project.presentation.event.splash

sealed class SplashNavigationEvents {
    object NavigateToLogIn : SplashNavigationEvents()
    object NavigateToHome : SplashNavigationEvents()
    object Pending : SplashNavigationEvents()
}
