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
    private val observations: MutableList<Observation>,
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
        val observation = observations[position]

        holder.tvName.text = observation.astroName
        holder.tvCategory.text = observation.category
        holder.tvTimestamp.text = observation.timestamp
        holder.tvCoords.text = "📍 %.4f°, %.4f°".format(observation.latitude, observation.longitude)
        holder.tvNotes.text = observation.notes
        holder.ivCategoryIcon.setImageResource(getCategoryIcon(observation.category))

        if (observation.photoUri.isNullOrBlank()) {
            holder.ivPhoto.setImageResource(R.drawable.bg_space_placeholder)
        } else {
            Glide.with(holder.itemView.context)
                .load(Uri.parse(observation.photoUri))
                .placeholder(R.drawable.bg_space_placeholder)
                .error(R.drawable.bg_space_placeholder)
                .centerCrop()
                .into(holder.ivPhoto)
        }

        holder.itemView.setOnClickListener { onItemClick(observation) }
    }

    override fun getItemCount(): Int = observations.size

    fun updateData(newObservations: List<Observation>) {
        observations.clear()
        observations.addAll(newObservations)
        notifyDataSetChanged()
    }

    private fun getCategoryIcon(category: String): Int {
        return when (category.lowercase()) {
            "planeta" -> R.drawable.ic_planet
            "constelação", "constelacao" -> R.drawable.ic_constellation
            "galáxia", "galaxia" -> R.drawable.ic_galaxy
            "cometa" -> R.drawable.ic_comet
            "estrela" -> R.drawable.ic_star_icon
            else -> R.drawable.ic_telescope
        }
    }
}
