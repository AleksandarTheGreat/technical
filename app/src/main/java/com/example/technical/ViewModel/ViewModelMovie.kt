package com.example.technical.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.technical.Api.MovieApi
import com.example.technical.Model.Movie
import com.example.technical.Model.MovieDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.annotations.Async

class ViewModelMovie : ViewModel() {

    val mutableLiveDataMovies: MutableLiveData<List<Movie>> = MutableLiveData();
    val mutableLiveDataMovieDetails: MutableLiveData<MovieDetails> = MutableLiveData();
    private var movieApi = MovieApi.getInstance();

    fun loadAll(onSuccess: () -> Unit, onFailure: (String) -> Unit){
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO){
                    movieApi.fetchMoviesResponse()
                }

                mutableLiveDataMovies.value = response.results;
                onSuccess();

            } catch (e: Exception) {
                onFailure("Failed to load movies");
            }
        }
    }

    fun loadMovieDetails(movie: Movie, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO){
                    movieApi.fetchMovieDetails(movie.id)
                }

                mutableLiveDataMovieDetails.value = response;
                onSuccess();

            } catch (e: Exception){
                onFailure("Failed to load movie details about '${movie.title}'");
            }
        }
    }

    fun loadSearchedMovies(query: String, onSuccess: () -> Unit, onFailure: (String) -> Unit){
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO){
                    movieApi.fetchSearchedMovies(query)
                }

                mutableLiveDataMovies.value = response.results;
                onSuccess();

            } catch (e: Exception){
                onFailure("Failed to load movies about '${query}'");
            }
        }
    }

    fun loadGenreMovies(genre: Int, onSuccess: () -> Unit, onFailure: (String) -> Unit){
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO){
                    movieApi.fetchGenreMovies(genre);
                }

                mutableLiveDataMovies.value = response.results;
                onSuccess();

            } catch (e: Exception){
                onFailure("Failed to load movies for this category");
            }
        }
    }

}