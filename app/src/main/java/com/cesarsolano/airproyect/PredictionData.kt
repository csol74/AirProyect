package com.cesarsolano.airproyect

data class PredictionRequest(
    val PM10: Float,
    val PM2_5: Float,
    val NO2: Float,
    val O3: Float
)
