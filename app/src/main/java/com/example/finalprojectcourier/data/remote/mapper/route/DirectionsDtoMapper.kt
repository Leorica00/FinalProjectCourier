package com.example.final_project.data.remote.mapper.route

import com.example.finalprojectcourier.data.remote.model.DirectionsResponseDto
import com.example.finalprojectcourier.domain.model.GetDirection

fun DirectionsResponseDto.toDomain() = GetDirection(
    routes = routes,
)