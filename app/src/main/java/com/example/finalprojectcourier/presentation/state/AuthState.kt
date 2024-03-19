package com.example.final_project.presentation.state

import com.google.firebase.auth.AuthResult

data class AuthState (
    val isLoading: Boolean = false,
    val data: AuthResult? = null,
    val errorMessage: Int? = null
)
