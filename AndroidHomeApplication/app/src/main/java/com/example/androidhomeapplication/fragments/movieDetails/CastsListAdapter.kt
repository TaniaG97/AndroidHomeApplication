package com.example.androidhomeapplication.fragments.movieDetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidhomeapplication.R
import com.example.androidhomeapplication.models.CastData

class CastsListAdapter(
    var items: List<CastData>
) : RecyclerView.Adapter<CastsListAdapter.CastItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_movie_cast, parent, false)
        return CastItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CastItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    inner class CastItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var image: ImageView = itemView.findViewById(R.id.item_cast_image)
        private var textName: TextView = itemView.findViewById(R.id.item_cast_text)

        fun bind(castData: CastData) {
            image.setImageResource(castData.image)
            textName.text = castData.name
        }
    }

}