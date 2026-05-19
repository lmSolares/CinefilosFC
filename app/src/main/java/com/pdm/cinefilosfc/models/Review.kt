package com.pdm.cinefilosfc.models

data class Review(
    val id: Int,
    val movieTitle: String,
    val reviewText: String,
    val rating: Float,
    val posterPath: String,
    val releaseYear: String,
    val fecha: String
)