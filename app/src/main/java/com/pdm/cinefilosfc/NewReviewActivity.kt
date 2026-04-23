package com.pdm.cinefilosfc

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso

class NewReviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_review)

        val tvTitle = findViewById<TextView>(R.id.tv_movie_title)
        val ivPoster = findViewById<ImageView>(R.id.iv_movie_poster)
        val btnCancel = findViewById<TextView>(R.id.btn_cancel)

        val title = intent.getStringExtra("MOVIE_TITLE")
        val posterPath = intent.getStringExtra("MOVIE_POSTER")

        tvTitle.text = title ?: "Sin título"

        if (posterPath != null) {
            val imageUrl = "https://image.tmdb.org/t/p/w500$posterPath"
            Picasso.get()
                .load(imageUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(ivPoster)
        }

        btnCancel.setOnClickListener {
            finish()
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.selectedItemId = R.id.nav_profile
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_profile -> {
                    true
                }
                else -> false
            }
        }
    }
}