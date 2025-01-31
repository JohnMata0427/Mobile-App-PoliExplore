package com.jidasea.poliexplore.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jidasea.poliexplore.R
import com.jidasea.poliexplore.models.Place

class PlaceAdapter(private var place: Place):  RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.title)
        val idTextView: TextView = itemView.findViewById(R.id.building_id)
        val imageView: ImageView = itemView.findViewById(R.id.image)
        val descriptionTextView: TextView = itemView.findViewById(R.id.description_content)
        val funFactsTextView: TextView = itemView.findViewById(R.id.fun_facts_content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_result, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = 1

    override fun onBindViewHolder(holder: PlaceAdapter.ViewHolder, position: Int) {
        holder.idTextView.text = "Building ID: ${place.id}"
        holder.titleTextView.text = place.name
        holder.imageView.setImageURI(Uri.parse(place.imageUrl))
        holder.descriptionTextView.text = place.description
        holder.funFactsTextView.text = place.funFacts.joinToString("\n\n")
    }

}