package com.example.final_project.presentation.event

import com.google.firebase.auth.PhoneAuthOptions

sealed class LoginEvent {
    data class SignInUserWithCredential(val credential: String, val options: PhoneAuthOptions.Builder) : LoginEvent()
}
