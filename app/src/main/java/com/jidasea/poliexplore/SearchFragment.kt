package com.jidasea.poliexplore

import SearchAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jidasea.poliexplore.viewmodels.SearchPlaceViewModel

class SearchFragment : Fragment() {
    private val searchPlaceViewModel = SearchPlaceViewModel()
    private lateinit var adapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.fragment_search, container, false)
        val searchResultsRecyclerView = view.findViewById<RecyclerView>(R.id.searchResultsRecyclerView)
        val searchView: SearchView = view.findViewById(R.id.searchView)

        searchResultsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        searchResultsRecyclerView.adapter = SearchAdapter(emptyList(), parentFragmentManager).also { adapter = it }

        searchPlaceViewModel.fetchSearchPlaces()

        searchPlaceViewModel.filteredPlaces.observe(viewLifecycleOwner) {
            adapter.updateResults(it)
        }

        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) searchResultsRecyclerView.visibility = View.VISIBLE
            else searchResultsRecyclerView.visibility = View.GONE
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchPlaceViewModel.filterPlaces(newText ?: "")
                return true
            }
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentTitleTextView: TextView? = activity?.findViewById(R.id.fragment_title)
        fragmentTitleTextView?.text = "Buscar Edificios"
    }
}
