package com.example.finalprojectcourier.domain.usecase.validators

import javax.inject.Inject

class PhoneNumberValidatorUseCase @Inject constructor() {
    operator fun invoke(phoneNumber: String) : Boolean {
        val phoneNumberPattern = Regex("^\\+995[0-9]{9}\$")
        return phoneNumberPattern.matches(phoneNumber)
    }
}