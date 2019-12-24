package com.mark.sharee.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.mark.sharee.network.model.responses.MovieResponse

class MovieAdapter() :
    ListAdapter<MovieResponse.Movie, MovieAdapterViewHolder>(
        DIFF_CALLBACK
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MovieAdapterViewHolder.create(parent)

    override fun onBindViewHolder(holderAdapter: MovieAdapterViewHolder, position: Int) {
        holderAdapter.bind(holderAdapter.containerView, getItem(position))

    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MovieResponse.Movie>() {

            override fun areItemsTheSame(
                oldItem: MovieResponse.Movie,
                newItem: MovieResponse.Movie
            ) = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: MovieResponse.Movie,
                newItem: MovieResponse.Movie
            ) =
                oldItem == newItem
        }
    }
}
