package com.example.finalproject

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CurrencyExchangeService {
    @GET("businesses/search")
    fun GetExchangeRate(@Query("term") searchTerm: String, @Query("location") location: String): Call<Int>
}