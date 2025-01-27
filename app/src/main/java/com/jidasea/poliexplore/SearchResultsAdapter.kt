package com.jidasea.poliexplore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jidasea.poliexplore.R
import com.jidasea.poliexplore.SearchResult

class SearchResultsAdapter(private val results: List<Place>) :
    RecyclerView.Adapter<SearchResultsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val numeroEdificio: TextView = itemView.findViewById(R.id.numeroEdificio)
        val nombreEdificio: TextView = itemView.findViewById(R.id.nombreEdificio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_result, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.numeroEdificio.text = result.id.toString()
        holder.nombreEdificio.text = result.name
    }

    override fun getItemCount(): Int = results.size
}