package com.example.finalprojectcourier.domain.usecase.validators

import javax.inject.Inject

class EmailValidationUseCase @Inject constructor() {
    private val emailPattern = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+$")

    operator fun invoke(email: String) : Boolean {
        return emailPattern.matches(email)
    }
}