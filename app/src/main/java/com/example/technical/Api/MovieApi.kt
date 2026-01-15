package com.example.technical.Api

import com.example.technical.Model.Movie
import com.example.technical.Model.MovieDetails
import com.example.technical.Model.MoviesResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("movie/popular")
    suspend fun fetchMoviesResponse(
        @Query("api_key") apiKey: String = API_KEY,
    ): MoviesResponse;

    @GET("movie/{id}")
    suspend fun fetchMovieDetails(
        @Path("id") movieId: Long,
        @Query("api_key") apiKey: String = API_KEY,
    ): MovieDetails;

    @GET("search/movie")
    suspend fun fetchSearchedMovies(
        @Query("query") query: String,
        @Query("api_key") apiKey: String = API_KEY,
    ): MoviesResponse;

    @GET("discover/movie")
    suspend fun fetchGenreMovies(
        @Query("with_genres") genres: Int,
        @Query("api_key") apiKey: String = API_KEY,
    ): MoviesResponse;

    companion object {
        private var instance: MovieApi? = null;
        val BASE_POSTER_ENDPOINT: String = "https://image.tmdb.org/t/p/w500/";
        private val BASE_ENDPOINT = "https://api.themoviedb.org/3/";
        private val API_KEY = "3621b97015d6f2bc8dad5031f853d514"

        fun getInstance(): MovieApi {
            if (instance == null){
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

                instance = retrofit.create(MovieApi::class.java);
            }
            return instance!!;
        }
    }

}