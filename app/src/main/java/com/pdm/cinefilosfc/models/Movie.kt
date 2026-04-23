package com.pdm.cinefilosfc.models

import com.google.gson.annotations.SerializedName

data class TmdbResponse(
    val results: List<Movie>
)

data class Movie(
    @SerializedName("title", alternate = ["name"])
    val title: String?,

    @SerializedName("release_date", alternate = ["first_air_date"])
    val releaseDate: String?,

    @SerializedName("poster_path")
    val posterPath: String?
)