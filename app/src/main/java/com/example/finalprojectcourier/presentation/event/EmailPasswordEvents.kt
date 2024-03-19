package com.example.final_project.presentation.event

sealed class EmailPasswordEvents {
    data class SignInUsingEmailAndPassword(val email: String, val password: String) : EmailPasswordEvents()
}
