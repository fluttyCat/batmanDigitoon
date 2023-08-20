package com.core.dto

import androidx.annotation.Keep


@Keep
data class Coordinate(
    @Keep val lat: Double,
    @Keep val lng: Double
)