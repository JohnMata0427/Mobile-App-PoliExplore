package com.jidasea.poliexplore

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jidasea.poliexplore.SearchResultsAdapter

class SearchFragment : Fragment() {
    private lateinit var searchResultsRecyclerView: RecyclerView
    private lateinit var searchResultsAdapter: SearchResultsAdapter
    private val searchResults = listOf(
        SearchResult("Edificio 21", "Escuela de formacion de tecnologos"),
        SearchResult("Edificio 22", "Facultad de Ingenieria"),
        SearchResult("Edificio 23", "Biblioteca Central")
    )
    private var filteredResults = mutableListOf<SearchResult>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
                        it.numeroEdificio.lowercase().contains(query) || it.nombreEdificio.lowercase().contains(query)
                    })
                }
                searchResultsAdapter.notifyDataSetChanged()
                return true
            }
        })

        return view
    }
}