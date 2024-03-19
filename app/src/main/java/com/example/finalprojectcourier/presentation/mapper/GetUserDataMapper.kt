package com.example.final_project.presentation.mapper

import com.example.final_project.domain.model.GetUserData
import com.example.final_project.presentation.model.UserData

fun GetUserData.toPresentation() : UserData {
    return UserData(email = email, phoneNumber = phoneNumber, fullName = fullName)
}