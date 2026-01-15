package com.example.technical.Model

import java.util.Objects

data class MovieDetails(
    val id: Long,
    val poster_path: String,
    val title: String,
    val overview: String,
    val release_date: String,
    val vote_average: Double,
    val vote_count: Long,
    val runtime: Long?
)