package com.example.finalprojectcourier.presentation.event.signup

import com.example.finalprojectcourier.presentation.model.ErrorType

sealed class SendUserDataEvent {
    data class SendUserData(val email: String, val password: String, val fullName: String) : SendUserDataEvent()
    class UpdateErrorMessage(val message: Int?, val errorType: ErrorType): SendUserDataEvent()
}