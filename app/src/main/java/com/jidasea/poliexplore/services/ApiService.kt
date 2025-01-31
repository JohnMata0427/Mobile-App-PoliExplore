package com.jidasea.poliexplore.services

import com.jidasea.poliexplore.models.Place
import com.jidasea.poliexplore.models.Search
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

object RetroFitClient {
    private const val BASE_URL = "https://epn-places-information-restful-api.onrender.com/"
    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

interface ApiService {
    @GET("places")
    suspend fun getAllPlaces(): List<Search>
    @GET("places/{id}")
    suspend fun getPlaceById(@Path("id") id: Int): Place
}