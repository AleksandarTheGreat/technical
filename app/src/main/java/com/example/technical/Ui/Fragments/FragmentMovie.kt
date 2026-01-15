package com.example.technical.Ui.Fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentResultOwner
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.technical.Api.MovieApi
import com.example.technical.Helper.IEssentials
import com.example.technical.Helper.Toaster
import com.example.technical.Model.Movie
import com.example.technical.Model.MovieDetails
import com.example.technical.R
import com.example.technical.ViewModel.ViewModelMovie
import com.example.technical.databinding.FragmentMovieBinding

class FragmentMovie : Fragment(), IEssentials {

    private lateinit var binding: FragmentMovieBinding;
    private lateinit var movie: Movie;
    private val viewModelMovie: ViewModelMovie by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieBinding.bind(inflater.inflate(R.layout.fragment_movie, container, false));

        instantiateObjects()
        addEventListeners()

        return binding.root
    }

    override fun instantiateObjects() {
        val builder = ProgressDialog(requireContext());
        builder.setTitle("Loading Details...");
        builder.setCancelable(false);
        builder.show();

        viewModelMovie.loadMovieDetails(movie, {
            builder.dismiss();
            Toaster.text(requireContext(),"Successfully loaded movie details");
        }, { message ->
            builder.dismiss();
            Toaster.alert(requireContext(), message);
        });

        viewModelMovie.mutableLiveDataMovieDetails.observe(this) { movieDetails ->
            if (movieDetails == null){
                return@observe;
            }

            Glide.with(binding.imageViewPoster.context)
                .load("${MovieApi.BASE_POSTER_ENDPOINT}${movieDetails.poster_path}")
                .into(binding.imageViewPoster);

            binding.textViewTitle.text = movieDetails.title;
            binding.textViewOverview.text = "\uD83E\uDD14 ${movieDetails.overview}";
            binding.textViewReleaseDate.text = "\uD83D\uDCC5 ${movieDetails.release_date}";
            binding.textViewVoteCount.text = "✌\uFE0F ${movieDetails.vote_count}";
            binding.textViewVoteAverage.text = "⭐ ${movieDetails.vote_average}";
            binding.textViewRuntime.text = "⏰ ${movieDetails.runtime} minutes"
        }
    }

    override fun addEventListeners() {
        binding.imageViewBack.setOnClickListener {
            parentFragmentManager.popBackStack();
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(movie: Movie): FragmentMovie {
            return FragmentMovie().apply {
                this.movie = movie;
            }
        }
    }
}