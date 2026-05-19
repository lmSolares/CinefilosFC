package com.pdm.cinefilosfc

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso
import java.time.LocalDate
import java.util.Calendar

class NewReviewActivity : AppCompatActivity() {

    private lateinit var db: CinefilosDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_review)

        db = CinefilosDB(this)

        val tvTitle = findViewById<TextView>(R.id.tv_movie_title)
        val ivPoster = findViewById<ImageView>(R.id.iv_movie_poster)
        val btnCancel = findViewById<TextView>(R.id.btn_cancel)

        val etReview = findViewById<EditText>(R.id.et_review)
        val ratingBar = findViewById<RatingBar>(R.id.rating_bar)
        val btnPublish = findViewById<Button>(R.id.btn_publish)

        val title = intent.getStringExtra("MOVIE_TITLE") ?: "Sin título"
        val posterPath = intent.getStringExtra("MOVIE_POSTER")
        val currentUser = intent.getStringExtra("LOGGED_IN_USERNAME") ?: "admin"

        tvTitle.text = title

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

        btnPublish.setOnClickListener {
            val reviewText = etReview.text.toString().trim()
            val rating = ratingBar.rating

            if (reviewText.isNotEmpty()) {
                val fechaActual = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(java.util.Date())
                val exito = db.insertarResena(currentUser, title, reviewText, rating, posterPath,
                    Calendar.getInstance().get(Calendar.YEAR).toString(), fechaActual)
                if (exito) {
                    Toast.makeText(this, "¡Reseña publicada!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Error al guardar la reseña", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor escribe una reseña", Toast.LENGTH_SHORT).show()
            }
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.selectedItemId = R.id.nav_profile
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, MainActivity::class.java).apply {
                        putExtra("LOGGED_IN_USERNAME", currentUser)
                    }
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