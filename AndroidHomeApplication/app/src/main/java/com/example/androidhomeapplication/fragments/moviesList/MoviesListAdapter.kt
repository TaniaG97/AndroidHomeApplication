package com.example.androidhomeapplication.fragments.moviesList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.androidhomeapplication.R
import com.example.androidhomeapplication.databinding.ItemMovieCastBinding
import com.example.androidhomeapplication.databinding.ItemMoviesListBinding
import com.example.androidhomeapplication.models.MovieData
import com.example.androidhomeapplication.setImageActiveState
import com.example.androidhomeapplication.setRating

class MoviesListAdapter(
    private val onItemClick: ((MovieData) -> Unit)
) : ListAdapter<MovieData, MovieItemViewHolder>(TaskDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_movies_list, parent, false)
        return MovieItemViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: MovieItemViewHolder, position: Int) = holder.bind(getItem(position))
}

class TaskDiffCallBack : DiffUtil.ItemCallback<MovieData>() {
    override fun areItemsTheSame(oldItem: MovieData, newItem: MovieData): Boolean = (oldItem.id == newItem.id)
    override fun areContentsTheSame(oldItem: MovieData, newItem: MovieData): Boolean = (oldItem === newItem)
}

class MovieItemViewHolder(
    itemView: View,
    private val onItemClick: ((MovieData) -> Unit)
) : RecyclerView.ViewHolder(itemView) {

    private val binding by viewBinding(ItemMoviesListBinding::bind)
    private var itemData: MovieData?=null

    init {
        itemView.setOnClickListener {
            itemData?.let(onItemClick)
        }
    }

    fun bind(itemData: MovieData) {
        this.itemData = itemData
        binding.itemImage.setImageResource(itemData.posterResId)
        binding.itemTextAge.text = itemView.context.getString(R.string.age_template, itemData.ageLimit)
        binding.itemIconLike.setImageActiveState(isActive = itemData.isLiked)
        binding.itemMoveTypes.text = itemData.genresList.joinToString(", ")
        binding.itemStars.setRating(itemData.starCount)
        binding.itemTextReviews.text = itemView.context?.getString(R.string.reviews_template, itemData.reviewCount)
        binding.itemTextTitle.text = itemData.title
        binding.itemMoveTime.text = itemView.context?.getString(R.string.time_template, itemData.time)
    }
}