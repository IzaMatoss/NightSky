package com.example.nightsky.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nightsky.R
import com.example.nightsky.models.AstroObject

class ExploreAdapter(
    private val items: List<AstroObject>,
    private val onItemClick: (AstroObject) -> Unit
) : RecyclerView.Adapter<ExploreAdapter.ExploreViewHolder>() {

    inner class ExploreViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivIcon: ImageView = view.findViewById(R.id.ivExploreIcon)
        val tvName: TextView = view.findViewById(R.id.tvExploreName)
        val tvType: TextView = view.findViewById(R.id.tvExploreType)
        val tvDescription: TextView = view.findViewById(R.id.tvExploreDescription)
        val tvVisibility: TextView = view.findViewById(R.id.tvVisibility)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreViewHolder {
        // Certifique-se que o arquivo res/layout/item_explore.xml existe
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_explore, parent, false)
        return ExploreViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExploreViewHolder, position: Int) {
        val item = items[position]
        holder.tvName.text = item.name
        holder.tvType.text = item.type
        holder.tvDescription.text = item.description
        holder.tvVisibility.text = item.visibility

        // Define o ícone (o AstroObject deve ter uma propriedade iconRes com o ID do drawable)
        holder.ivIcon.setImageResource(item.iconRes)

        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    override fun getItemCount() = items.size
}