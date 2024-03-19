package com.example.final_project.presentation.state

data class LoginState(
    val isLoading: Boolean = false,
    val errorMessage: Int? = null
)
