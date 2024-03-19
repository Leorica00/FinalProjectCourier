package com.example.finalprojectcourier.presentation.state

import androidx.annotation.StringRes

data class AdditionalDataState(
    @StringRes val emailErrorMessage: Int? = null,
    @StringRes val fullNameErrorMessage: Int? = null,
    @StringRes val passwordErrorMessage: Int? = null,
    val isLoading: Boolean = false,
    @StringRes val errorMessage: Int? = null
)