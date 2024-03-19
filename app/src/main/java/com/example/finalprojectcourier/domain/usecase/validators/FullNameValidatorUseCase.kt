package com.example.finalprojectcourier.domain.usecase.validators

import javax.inject.Inject

class FullNameValidatorUseCase @Inject constructor() {
    operator fun invoke(fullName: String): Boolean {
        val regex = Regex("^[a-zA-Z]+\\s[a-zA-Z]+\$")
        return regex.matches(fullName)
    }
}