package com.example.technical.Ui.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.technical.Api.MovieApi
import com.example.technical.Helper.Toaster
import com.example.technical.Model.Movie
import com.example.technical.R;
import com.example.technical.Ui.Fragments.FragmentMovie

class MovieAdapter(
    private val context: Context,
    private val parentFragmentManager: FragmentManager,
    private val movies: List<Movie>,
): RecyclerView.Adapter<MovieAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val constraintLayoutMain: ConstraintLayout = itemView.findViewById<ConstraintLayout>(R.id.constraintLayoutSingleMovie);
        val imageViewPoster: ImageView = itemView.findViewById<ImageView>(R.id.imageViewPoster);
        val textViewTitle: TextView = itemView.findViewById<TextView>(R.id.textViewTitle);
        val textViewRating: TextView = itemView.findViewById<TextView>(R.id.textViewRating);
        val textViewReleaseDate: TextView = itemView.findViewById<TextView>(R.id.textViewReleaseDate);
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieAdapter.MyViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_movie, parent, false);

        val myViewHolder = MyViewHolder(view);
        myViewHolder.constraintLayoutMain.setOnClickListener {
            val movie = movies[myViewHolder.absoluteAdapterPosition];

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, FragmentMovie.newInstance(movie))
                .setReorderingAllowed(true)
                .addToBackStack("backstack")
                .commit();
        }
        return myViewHolder;
    }

    override fun onBindViewHolder(holder: MovieAdapter.MyViewHolder, position: Int) {
        val movie = movies[position];

        Glide.with(holder.imageViewPoster.context)
            .load("${MovieApi.BASE_POSTER_ENDPOINT}${movie.poster_path}")
            .into(holder.imageViewPoster);
        holder.textViewTitle.text = movie.title
        holder.textViewRating.text = "⭐ ${movie.vote_average}"
        holder.textViewReleaseDate.text = "\uD83D\uDCC5 ${movie.release_date}";
    }

    override fun getItemCount(): Int {
        return movies.size;
    }
}