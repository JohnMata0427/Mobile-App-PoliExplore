package com.jidasea.poliexplore.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jidasea.poliexplore.models.Search
import com.jidasea.poliexplore.services.RetroFitClient
import kotlinx.coroutines.launch

class SearchPlaceViewModel: ViewModel() {
    private val _searchPlaces = MutableLiveData<List<Search>>()
    val searchPlaces: LiveData<List<Search>> get() = _searchPlaces

    private val _filteredPlaces = MutableLiveData<List<Search>>()
    val filteredPlaces: LiveData<List<Search>> get() = _filteredPlaces

    fun fetchSearchPlaces() {
        viewModelScope.launch {
            try {
                _searchPlaces.value = RetroFitClient.apiService.getAllPlaces()
                _filteredPlaces.value = _searchPlaces.value
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun filterPlaces(query: String) {
        val list = _searchPlaces.value ?: emptyList()
        _filteredPlaces.value = if (query.isEmpty()) {
            list
        } else {
            list.filter {
                it.name.contains(query, ignoreCase = true) || it.id.toString().contains(query)
            }
        }
    }
}