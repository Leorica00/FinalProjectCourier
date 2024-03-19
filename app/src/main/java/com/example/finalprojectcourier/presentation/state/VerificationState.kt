package com.example.final_project.presentation.state

data class VerificationState(
    val isLoading: Boolean = false,
    val data: String? = null,
    val errorMessage: Int? = null,
    val phoneErrorMessage: Int? = null
)