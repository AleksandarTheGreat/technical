package com.example.technical.Model

data class MoviesResponse(
    val page: Long,
    val total_pages: Long,
    val total_results: Long,
    val results: List<Movie>
)