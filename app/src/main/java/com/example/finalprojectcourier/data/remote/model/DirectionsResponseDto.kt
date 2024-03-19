package com.example.finalprojectcourier.data.remote.model

data class DirectionsResponseDto(
    val routes: List<Route>,
) {
    data class Route(
        val overview_polyline: OverviewPolyline,
    )

    data class OverviewPolyline(
        val points: String
    )
}

