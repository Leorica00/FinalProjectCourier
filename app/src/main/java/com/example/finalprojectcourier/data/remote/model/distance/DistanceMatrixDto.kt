package com.example.finalprojectcourier.data.remote.model.distance

data class DistanceMatrixDto(
    val rows: List<RowDto>
) {
    data class RowDto(
        val elements: List<ElementDto>?
    )

    data class ElementDto(
        val distance: DistanceDto?,
        val duration: DurationDto?
    )

    data class DistanceDto(
        val text: String,
        val value: Int
    )

    data class DurationDto(
        val text: String,
        val value: Int
    )
}

