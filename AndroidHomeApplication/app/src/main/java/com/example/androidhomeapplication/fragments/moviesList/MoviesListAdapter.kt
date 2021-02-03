package com.example.androidhomeapplication.fragments.moviesList

import android.content.res.ColorStateList
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.androidhomeapplication.R
import com.example.androidhomeapplication.Utils
import com.example.androidhomeapplication.databinding.ItemMovieCastBinding
import com.example.androidhomeapplication.databinding.ItemMoviesListBinding
import com.example.androidhomeapplication.models.MovieData

class MoviesListAdapter(
    private val items: List<MovieData>,
    private val onItemClick: ((MovieData) -> Unit)? = null
) : RecyclerView.Adapter<MovieItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_movies_list, parent, false)
        return MovieItemViewHolder(view, onItemClick)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: MovieItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }
}

class MovieItemViewHolder(
    itemView: View,
    private val onItemClick: ((MovieData) -> Unit)?
) : RecyclerView.ViewHolder(itemView) {

    private val binding = ItemMoviesListBinding.bind(itemView)
    private var stars: List<ImageView> = listOf(
        itemView.findViewById(R.id.star_1),
        itemView.findViewById(R.id.star_2),
        itemView.findViewById(R.id.star_3),
        itemView.findViewById(R.id.star_4),
        itemView.findViewById(R.id.star_5)
    )

    fun bind(itemData: MovieData) {
        binding.itemImage.setImageResource(itemData.poster)
        binding.itemTextAge.text = itemView.context?.getString(R.string.age_template, itemData.ageLimit)
        Utils.setImageActiveState(itemData.isLiked, binding.itemIconLike)
        binding.itemMoveTypes.text = itemData.genresList.joinToString(", ")

        stars.forEachIndexed { index, imageView ->
            if (index < itemData.starCount) {
                Utils.setImageActiveState(true, imageView)
            } else {
                Utils.setImageActiveState(false, imageView)
            }
        }

        binding.itemTextReviews.text = itemView.context?.getString(R.string.reviews_template, itemData.reviewCount)
        binding.itemTextTitle.text = itemData.title
        binding.itemMoveTime.text = itemView.context?.getString(R.string.time_template, itemData.time)

        itemView.setOnClickListener {
            onItemClick?.invoke(itemData)
        }
    }
}