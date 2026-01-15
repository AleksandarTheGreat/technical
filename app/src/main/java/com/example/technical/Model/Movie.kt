package com.example.technical.Model

import java.util.Objects

data class Movie(
    val id: Long,
    val poster_path: String,
    val title: String,
    val release_date: String,
    val vote_average: Double?
)