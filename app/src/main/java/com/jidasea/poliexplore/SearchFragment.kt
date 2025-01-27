package com.jidasea.poliexplore

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jidasea.poliexplore.SearchResultsAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private lateinit var searchResultsRecyclerView: RecyclerView
    private lateinit var searchResultsAdapter: SearchResultsAdapter
    private val _place = MutableLiveData<List<Place>?>()
    val places: LiveData<List<Place>?> get() = _place
    private val repository = PlaceRepository()
    fun getAllPlaces() {
        lifecycleScope.launch {
            try {
                val places = repository.getPlaces()
                println(places)
                _place.postValue(places)
            } catch (e: Exception) {
                println(e)
                _place.postValue(null)
            }
        }
    }
    fun getPlaceById(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val place = repository.getPlaceById(id)
                println(place)
            } catch (e: Exception) {
                println(e)
            }
        }
    }

    private lateinit var searchResults: List<Place>

    private var filteredResults = mutableListOf<Place>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        places.observe(viewLifecycleOwner) {
            searchResults = it ?: emptyList()
        }
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        searchResultsRecyclerView = view.findViewById(R.id.searchResultsRecyclerView)
        searchResultsAdapter = SearchResultsAdapter(filteredResults)
        searchResultsRecyclerView.layoutManager = LinearLayoutManager(context)
        searchResultsRecyclerView.adapter = searchResultsAdapter

        val searchView: SearchView = view.findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                filteredResults.clear()
                if (!newText.isNullOrEmpty()) {
                    val query = newText.lowercase()
                    filteredResults.addAll(searchResults.filter {
                        it.id.toString().lowercase().contains(query) || it.name.lowercase().contains(query)
                    })
                }
                searchResultsAdapter.notifyDataSetChanged()
                return true
            }
        })

        return view
    }
}
