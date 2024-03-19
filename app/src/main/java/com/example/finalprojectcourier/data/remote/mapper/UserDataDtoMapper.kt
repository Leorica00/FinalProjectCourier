package com.example.final_project.data.remote.mapper

import com.example.final_project.data.remote.model.UserDataDto
import com.example.final_project.domain.model.GetUserData

fun UserDataDto.toDomain() : GetUserData {
    return GetUserData(email = email, phoneNumber = phoneNumber, fullName = fullName)
}