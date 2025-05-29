package com.cesarsolano.airproyect

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call

interface ApiService {
    @POST("/predict")
    fun getPrediction(@Body request: PredictionRequest): Call<PredictionResponse>
}
