package com.jidasea.poliexplore

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("places")
    suspend fun getPlaces(): List<Place>
    @GET("places/{id}")
    suspend fun getPlaceById(@Path("id") id: Int): Place
}