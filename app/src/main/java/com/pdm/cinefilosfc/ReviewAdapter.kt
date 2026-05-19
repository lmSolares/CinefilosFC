package com.pdm.cinefilosfc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pdm.cinefilosfc.models.Review
import com.squareup.picasso.Picasso

class ReviewAdapter(private var reviews: List<Review>) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    class ReviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tv_rev_title)
        val tvYear: TextView = view.findViewById(R.id.tv_rev_year)
        val tvText: TextView = view.findViewById(R.id.tv_rev_text)
        val tvDate: TextView = view.findViewById(R.id.tv_rev_date)
        val rbStars: RatingBar = view.findViewById(R.id.rb_rev_stars)
        val ivPoster: ImageView = view.findViewById(R.id.iv_rev_poster)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.tvTitle.text = review.movieTitle
        holder.tvYear.text = review.releaseYear
        holder.tvText.text = review.reviewText
        holder.tvDate.text = "Publicado: ${review.fecha}"
        holder.rbStars.rating = review.rating

        if (review.posterPath.isNotEmpty()) {
            val imageUrl = "https://image.tmdb.org/t/p/w500${review.posterPath}"
            Picasso.get().load(imageUrl).into(holder.ivPoster)
        }
    }

    override fun getItemCount() = reviews.size

    fun updateData(newReviews: List<Review>) {
        reviews = newReviews
        notifyDataSetChanged()
    }
}