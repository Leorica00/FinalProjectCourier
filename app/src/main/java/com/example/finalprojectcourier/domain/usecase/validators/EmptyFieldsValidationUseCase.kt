package com.example.finalprojectcourier.domain.usecase.validators

import javax.inject.Inject

class EmptyFieldsValidationUseCase @Inject constructor() {
    operator fun invoke(vararg field: String): Boolean {
        return field.all { it.isNotBlank() }
    }
}