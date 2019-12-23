package com.mark.sharee.network.model.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@kotlinx.serialization.Serializable
data class MovieResponse(@SerialName(value = "page") val page: Int, @SerialName(value = "results") val movies: ArrayList<Movie>) {

    @Serializable
    data class Movie(
        @SerialName(value = "id") val id: Long, @SerialName(value = "poster_path") val posterUrl: String, @SerialName(
            value = "original_title"
        ) val name: String, @SerialName(
            value = "vote_average"
        ) val rating: Float, @SerialName(value = "overview") val description: String, @SerialName(
            value = "release_date"
        ) val releaseDate: String
    ) {
        var isFavorite: Boolean = false
    }
}