package com.pdm.cinefilosfc.models

data class Movie(
    val name: String?,
    val premiered: String?,
    val genres: List<String>?,
    val image: ImageUrl?
)

data class ImageUrl(
    val medium: String?
)