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
import com.example.androidhomeapplication.models.MovieData

class MoviesListAdapter(
    var items: List<MovieData>,
    var onItemClick: ((MovieData) -> Unit)? = null
) : RecyclerView.Adapter<MoviesListAdapter.MovieItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_movies_list, parent, false)
        return MovieItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MovieItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    inner class MovieItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var image: ImageView = itemView.findViewById(R.id.item_image)
        private var textAge: TextView = itemView.findViewById(R.id.item_text_age)
        private var iconLike: ImageView = itemView.findViewById(R.id.item_icon_like)
        private var moveTypes: TextView = itemView.findViewById(R.id.item_move_types)
        private var stars: List<ImageView> = listOf(
            itemView.findViewById(R.id.star_1),
            itemView.findViewById(R.id.star_2),
            itemView.findViewById(R.id.star_3),
            itemView.findViewById(R.id.star_4),
            itemView.findViewById(R.id.star_5)
        )
        private var textReviews: TextView = itemView.findViewById(R.id.item_text_reviews)
        private var textTitle: TextView = itemView.findViewById(R.id.item_text_title)
        private var moveTime: TextView = itemView.findViewById(R.id.item_move_time)

        fun bind(itemData: MovieData) {
            image.setImageResource(itemData.poster)
            textAge.text = "${itemData.ageLimit}+"
            iconLike.imageTintList = if (itemData.isLiked) {
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        iconLike.context,
                        R.color.radical_red
                    )
                )
            } else {
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        iconLike.context,
                        R.color.white_transparent
                    )
                )
            }
            moveTypes.text = itemData.genresList.joinToString(", ")
            stars.forEachIndexed { index, imageView ->
                imageView.imageTintList = if (index < itemData.starCount) {
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            iconLike.context,
                            R.color.radical_red
                        )
                    )
                } else {
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            iconLike.context,
                            R.color.storm_gray
                        )
                    )
                }
            }
            textReviews.text = "${itemData.reviewCount} Reviews"
            textTitle.text = itemData.title
            moveTime.text = "${itemData.time} MIN"

            itemView.setOnClickListener {
                onItemClick?.invoke(itemData)
            }
        }
    }

}