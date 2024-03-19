package com.example.final_project.presentation.event.signup

import android.app.Activity
import com.google.firebase.auth.PhoneAuthOptions

sealed class SendSmsEvent {
    data class SendSmsToProvidedNumber(val phoneNumber: String, val options: PhoneAuthOptions.Builder) : SendSmsEvent()
}