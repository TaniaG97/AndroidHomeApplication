package com.example.androidhomeapplication.fragments.movieDetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.androidhomeapplication.R
import com.example.androidhomeapplication.databinding.ItemMovieCastBinding
import com.example.androidhomeapplication.models.CastData

class CastsListAdapter() : ListAdapter<CastData, CastItemViewHolder>(TaskDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastItemViewHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.item_movie_cast, parent, false)
        return CastItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: CastItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TaskDiffCallBack : DiffUtil.ItemCallback<CastData>() {
        override fun areItemsTheSame(oldItem: CastData, newItem: CastData): Boolean {
            return oldItem.id == newItem.id;
        }

        override fun areContentsTheSame(oldItem: CastData, newItem: CastData): Boolean {
            return oldItem == newItem
        }
    }

}

class CastItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding = ItemMovieCastBinding.bind(itemView)

    fun bind(castData: CastData) {
        binding.itemCastImage.setImageResource(castData.image)
        binding.itemCastText.text = castData.name
    }
}