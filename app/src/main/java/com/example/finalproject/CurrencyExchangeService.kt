package com.example.finalproject

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CurrencyExchangeService {
    @GET("exchangeRate")
    fun GetExchangeRate(@Query("from") from: String, @Query("to") to: String): Call<Double>
}