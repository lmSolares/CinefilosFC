package com.pdm.cinefilosfc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pdm.cinefilosfc.models.Movie
import com.squareup.picasso.Picasso

class MovieAdapter(private val movies: List<Movie>) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivPoster: ImageView = view.findViewById(R.id.iv_item_poster)
        val tvTitle: TextView = view.findViewById(R.id.tv_item_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.tvTitle.text = movie.title ?: "Sin título"

        if (movie.posterPath != null) {
            val imageUrl = "https://image.tmdb.org/t/p/w500${movie.posterPath}"
            Picasso.get()
                .load(imageUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.ivPoster)
        }
    }

    override fun getItemCount() = movies.size
}