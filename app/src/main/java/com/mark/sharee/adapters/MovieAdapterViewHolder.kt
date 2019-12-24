package com.mark.sharee.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sharee.R
import com.mark.sharee.network.model.responses.MovieResponse
import com.mark.sharee.utils.GlideApp
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.single_movie_item.*

class MovieAdapterViewHolder
constructor(override val containerView: View) : RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    private fun setImageFavorite(isFavorite: Boolean) {
        if (isFavorite)
            movie_favorite_image_view.setImageResource(R.drawable.ic_star_golden_28dp)
        else
            movie_favorite_image_view.setImageResource(R.drawable.ic_star_border_golden_28dp)
    }

    fun bind(view: View, item: MovieResponse.Movie) {
        GlideApp.with(view.context)
            .load(MOVIE_POSTER_BASE_URL.plus(item.posterUrl))
            .fitCenter().centerCrop()
            .into(movie_poster_image_view)

        movie_name_text_view.text = item.name
        movie_release_date_text_view.text = item.releaseDate
        setImageFavorite(item.isFavorite)
        movie_favorite_image_view.setOnClickListener {
            item.isFavorite = !item.isFavorite
            setImageFavorite(item.isFavorite)
        }
    }

    companion object {
        private const val MOVIE_POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185"

        fun create(parent: ViewGroup): MovieAdapterViewHolder {
            return MovieAdapterViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.single_movie_item,
                    parent,
                    false)
            )
        }
    }
}