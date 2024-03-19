package com.example.finalprojectcourier.domain.model

import com.example.finalprojectcourier.data.remote.model.DirectionsResponseDto

data class GetDirection(
    val routes: List<DirectionsResponseDto.Route>,
)