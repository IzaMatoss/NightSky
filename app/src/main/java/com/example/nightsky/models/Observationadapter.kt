package com.example.nightsky

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ObservationAdapter(
    private var observations: MutableList<`Observation.kt`>,
    private val onItemClick: (`Observation.kt`) -> Unit
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
        val obs = observations[position]
        holder.tvName.text = obs.astroName
        holder.tvCategory.text = obs.category
        holder.tvTimestamp.text = obs.timestamp
        holder.tvCoords.text = "📍 %.4f°, %.4f°".format(obs.latitude, obs.longitude)
        holder.tvNotes.text = obs.notes

        // Set category icon (Certifique-se que esses ícones existem em res/drawable)
        val iconRes = when (obs.category.lowercase()) {
            "planeta" -> R.drawable.ic_planet
            "constelação", "constelacao" -> R.drawable.ic_constellation
            "galáxia", "galaxia" -> R.drawable.ic_galaxy
            "cometa" -> R.drawable.ic_comet
            "estrela" -> R.drawable.ic_star_icon
            else -> R.drawable.ic_telescope
        }
        holder.ivCategoryIcon.setImageResource(iconRes)

        // Load photo or default
        if (!obs.photoUri.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(Uri.parse(obs.photoUri))
                .placeholder(R.drawable.bg_space_placeholder)
                .error(R.drawable.bg_space_placeholder)
                .centerCrop()
                .into(holder.ivPhoto)
        } else {
            holder.ivPhoto.setImageResource(R.drawable.bg_space_placeholder)
        }

        holder.itemView.setOnClickListener { onItemClick(obs) }
    }

    override fun getItemCount() = observations.size

    fun updateData(newList: List<`Observation.kt`>) {
        observations.clear()
        observations.addAll(newList)
        notifyDataSetChanged()
    }
}