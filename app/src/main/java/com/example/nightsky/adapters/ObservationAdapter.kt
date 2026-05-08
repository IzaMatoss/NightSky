package com.example.nightsky.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nightsky.R
import com.example.nightsky.models.Observation

class ObservationAdapter(
    private var observations: MutableList<Observation>,
    private val onItemClick: (Observation) -> Unit
) : RecyclerView.Adapter<ObservationAdapter.ObservationViewHolder>() {

    inner class ObservationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivPhoto: ImageView = itemView.findViewById(R.id.ivObservationPhoto)
        val tvName: TextView = itemView.findViewById(R.id.tvAstroName)
        val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        val tvTimestamp: TextView = itemView.findViewById(R.id.tvTimestamp)
        val tvCoords: TextView = itemView.findViewById(R.id.tvCoordinates)
        val tvNotes: TextView = itemView.findViewById(R.id.tvNotes)
        val ivCategoryIcon: ImageView = itemView.findViewById(R.id.ivCategoryIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObservationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_observation, parent, false)

        return ObservationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ObservationViewHolder, position: Int) {
    }