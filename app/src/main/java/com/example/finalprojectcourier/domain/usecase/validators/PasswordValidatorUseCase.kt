package com.example.finalprojectcourier.domain.usecase.validators

import javax.inject.Inject

class PasswordValidatorUseCase @Inject constructor() {
    operator fun invoke(password: String): Boolean {
        val regex = Regex("^(?=.*[0-9])(?=.*[!@#\$%^&*])(?=\\S+\$).{6,}\$")
        return regex.matches(password)
    }
}