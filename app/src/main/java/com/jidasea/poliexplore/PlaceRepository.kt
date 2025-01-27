package com.jidasea.poliexplore

class PlaceRepository {
    private val apiService = RetroFitClient.apiService

    suspend fun getPlaces(): List<Place> {
        return apiService.getPlaces()
    }

    suspend fun getPlaceById(id: Int): Place {
        return apiService.getPlaceById(id)
    }
}