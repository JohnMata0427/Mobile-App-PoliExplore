package com.jidasea.poliexplore.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jidasea.poliexplore.models.Place
import androidx.lifecycle.viewModelScope
import com.jidasea.poliexplore.services.RetroFitClient
import kotlinx.coroutines.launch

class BuildingDetailsViewModel: ViewModel() {
    private val _place = MutableLiveData<Place>()
    val place: MutableLiveData<Place> get() = _place

    fun fetchPlace(id: Int) {
        viewModelScope.launch {
            try {
                _place.value = RetroFitClient.apiService.getPlaceById(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}