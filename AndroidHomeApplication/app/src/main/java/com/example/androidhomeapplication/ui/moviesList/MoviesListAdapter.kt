package com.example.androidhomeapplication.ui.moviesList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.androidhomeapplication.R
import com.example.androidhomeapplication.databinding.ItemMoviesListBinding
import com.example.androidhomeapplication.loadImageWithGlide
import com.example.androidhomeapplication.data.models.Movie
import com.example.androidhomeapplication.setImageActiveState
import com.example.androidhomeapplication.setRating

class MoviesListAdapter(
    private val onItemClick: ((Movie) -> Unit)
) : ListAdapter<Movie, MovieItemViewHolder>(TaskDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_movies_list, parent, false)
        return MovieItemViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: MovieItemViewHolder, position: Int) = holder.bind(getItem(position))
}

private class TaskDiffCallBack : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean = (oldItem.id == newItem.id)
    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean = (oldItem === newItem)
}

class MovieItemViewHolder(
    itemView: View,
    private val onItemClick: ((Movie) -> Unit)
) : RecyclerView.ViewHolder(itemView) {

    private val binding by viewBinding(ItemMoviesListBinding::bind)
    private var itemData: Movie?=null

    init {
        itemView.setOnClickListener {
            itemData?.let(onItemClick)
        }
    }

    fun bind(itemData: Movie) {
        this.itemData = itemData
        binding.itemImage.loadImageWithGlide(itemData.imageUrl)
        binding.itemTextAge.text = itemView.context.getString(R.string.age_template, itemData.pgAge)
        binding.itemIconLike.setImageActiveState(isActive = itemData.isLiked)
        binding.itemMoveTypes.text = itemData.genres.joinToString(", ") {genre ->  genre.name }
        binding.itemStars.setRating(itemData.rating)
        binding.itemTextReviews.text = itemView.context?.getString(R.string.reviews_template, itemData.reviewCount)
        binding.itemTextTitle.text = itemData.title
        binding.itemMoveTime.text = ""
    }
}