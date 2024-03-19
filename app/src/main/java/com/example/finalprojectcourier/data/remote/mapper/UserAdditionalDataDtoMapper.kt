package com.example.final_project.data.remote.mapper

import com.example.final_project.data.remote.model.UserAdditionalDataDto
import com.example.final_project.domain.model.GetUserAdditionalData

fun UserAdditionalDataDto.toDomain() : GetUserAdditionalData {
    return GetUserAdditionalData(fullName = fullName, email = email, password = password)
}