package com.jidasea.poliexplore

import com.bumptech.glide.Glide
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.jidasea.poliexplore.viewmodels.BuildingDetailsViewModel

class BuildingDetailsFragment: Fragment() {
    private val detailsViewModel = BuildingDetailsViewModel()
    private var placeId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            placeId = it.getInt("placeId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_building_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailsViewModel.fetchPlace(placeId?: 1)

        val fragmentTitleTextView: TextView? = activity?.findViewById(R.id.fragment_title)
        val titleTextView: TextView = view.findViewById(R.id.title)
        val idTextView: TextView = view.findViewById(R.id.building_id)
        val imageView: ImageView = view.findViewById(R.id.image)
        val descriptionTextView: TextView = view.findViewById(R.id.description_content)
        val funFactsTextView: TextView = view.findViewById(R.id.fun_facts_content)

        detailsViewModel.place.observe(viewLifecycleOwner) {
            fragmentTitleTextView?.text = if (it.id != 31) "Detalles del Edificio ${it.id}" else "Detalles de los Espacios 31, 32 y 33"
            titleTextView.text = it.name
            idTextView.text = if (it.id != 31) "Edificio ${it.id}" else "Espacios 31, 32 y 33"
            descriptionTextView.text = it.description
            funFactsTextView.text = "* ${it.funFacts.joinToString("\n\n* ")}"
            Glide.with(this)
                .load(it.imageUrl)
                .into(imageView)
        }
    }

    companion object {
        fun newInstance(placeId: Int) =
            BuildingDetailsFragment().apply {
                arguments = Bundle().apply {
                    putInt("placeId", placeId)
                }
            }
    }
}