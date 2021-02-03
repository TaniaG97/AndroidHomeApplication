package com.example.androidhomeapplication.fragments.movieDetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidhomeapplication.R
import com.example.androidhomeapplication.databinding.ItemMovieCastBinding
import com.example.androidhomeapplication.models.CastData

class CastsListAdapter(
    private val items: List<CastData>
) : RecyclerView.Adapter<CastItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_movie_cast, parent, false)
        return CastItemViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CastItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }
}

class CastItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding = ItemMovieCastBinding.bind(itemView)

    fun bind(castData: CastData) {
        binding.itemCastImage.setImageResource(castData.image)
        binding.itemCastText.text = castData.name
    }
}