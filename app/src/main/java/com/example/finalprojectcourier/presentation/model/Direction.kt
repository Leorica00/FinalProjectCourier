package com.example.finalprojectcourier.presentation.model

import com.example.finalprojectcourier.data.remote.model.DirectionsResponseDto

data class Direction(
    val routes: List<DirectionsResponseDto.Route>,
)
