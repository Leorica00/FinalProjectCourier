package com.example.final_project.presentation.mapper

import com.example.final_project.domain.model.GetUserAdditionalData
import com.example.final_project.presentation.model.AdditionalData

fun AdditionalData.toDomain() : GetUserAdditionalData {
    return GetUserAdditionalData(fullName = fullName, email = email, password = password)
}