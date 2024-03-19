package com.example.finalprojectcourier.presentation.mapper

import com.example.finalprojectcourier.domain.model.GetDirection
import com.example.finalprojectcourier.presentation.model.Direction

fun GetDirection.toPresentation() = Direction(
    routes = routes
)