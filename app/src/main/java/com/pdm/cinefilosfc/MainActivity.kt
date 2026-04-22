package com.pdm.cinefilosfc
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_review)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val tvTitle = findViewById<TextView>(R.id.tv_movie_title)
        val tvYear = findViewById<TextView>(R.id.tv_movie_year)
        val tvGenre = findViewById<TextView>(R.id.tv_movie_genre)
        val ivPoster = findViewById<ImageView>(R.id.iv_movie_poster)

        tvTitle.text = "Cargando..."
        tvYear.text = ""
        tvGenre.text = ""

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val pelicula = RetrofitClient.api.searchShow("The Office")

                withContext(Dispatchers.Main) {
                    if (pelicula != null) {
                        tvTitle.text = pelicula.name

                        val anio = pelicula.premiered?.take(4) ?: "Desconocido"
                        tvYear.text = anio

                        tvGenre.text = pelicula.genres?.joinToString(", ") ?: "Sin género"

                        Picasso.get()
                            .load(pelicula.image?.medium)
                            .placeholder(android.R.drawable.ic_menu_gallery)
                            .into(ivPoster)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    tvTitle.text = e.message ?: "Error de conexión"
                    e.printStackTrace()
                }
            }

        }
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    true
                }
                R.id.nav_search -> {
                    true
                }
                R.id.nav_profile -> {
                    val intent = Intent(this, PerfilActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_settings -> {
                    true
                }
                else -> false
            }
        }



    }
}


