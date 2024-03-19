package com.example.final_project.presentation.state

import com.example.final_project.presentation.model.Passcode

data class PasscodeState(
    val passcode: List<Passcode> = (1..6).map { Passcode(it) },
    val errorMessage: Int? = null,
    val successMessage: Boolean? = null
)