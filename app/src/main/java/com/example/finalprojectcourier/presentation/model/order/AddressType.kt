package com.example.final_project.presentation.model.delivery_location

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class AddressType(
    @StringRes val text: Int,
    @DrawableRes val icon: Int,
    @DrawableRes val selectedIcon: Int,
    val isSelected: Boolean = false
)
