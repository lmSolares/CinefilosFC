package com.pdm.cinefilosfc

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.pdm.cinefilosfc.models.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)

        toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        navigationView.setNavigationItemSelectedListener { menuItem ->
            val myApiKey = "ccdc150cc34be143bce60d00f3de897b"

            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val response = when (menuItem.itemId) {
                        R.id.nav_all -> RetrofitClient.api.getPopularMovies(myApiKey)
                        R.id.nav_movies -> RetrofitClient.api.getPopularMovies(myApiKey)
                        R.id.nav_series -> RetrofitClient.api.getPopularTVShows(myApiKey)
                        R.id.gen_horror -> RetrofitClient.api.getMoviesByGenre(myApiKey, "27")
                        R.id.gen_romance -> RetrofitClient.api.getMoviesByGenre(myApiKey, "10749")
                        R.id.gen_action -> RetrofitClient.api.getMoviesByGenre(myApiKey, "28")
                        else -> RetrofitClient.api.getPopularMovies(myApiKey)
                    }
                    withContext(Dispatchers.Main) {
                        configurarAdaptador(response.results)
                        drawerLayout.closeDrawers()
                    }
                } catch (e: Exception) { e.printStackTrace() }
            }
            true
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.selectedItemId = R.id.nav_home
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_profile -> {
                    startActivity(Intent(this, PerfilActivity::class.java))
                    true
                }
                else -> false
            }
        }

        val rvMovies = findViewById<RecyclerView>(R.id.rv_movies)
        val orientacion = resources.configuration.orientation
        val columnas = if (orientacion == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
            4
        } else {
            2
        }
        rvMovies.layoutManager = GridLayoutManager(this, columnas)

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val myApiKey = "ccdc150cc34be143bce60d00f3de897b"

                val response = RetrofitClient.api.getPopularMovies(myApiKey)


                withContext(Dispatchers.Main) {
                    configurarAdaptador(response.results)
                    drawerLayout.closeDrawers()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)

        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as androidx.appcompat.widget.SearchView

        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    ejecutarBusqueda(query)
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean = false
        })
        return true
    }

    private fun ejecutarBusqueda(query: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val myApiKey = "ccdc150cc34be143bce60d00f3de897b"
                val response = RetrofitClient.api.searchMulti(query, myApiKey)
                withContext(Dispatchers.Main) {
                    val rvMovies = findViewById<RecyclerView>(R.id.rv_movies)
                    configurarAdaptador(response.results)
                }
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                true
            }
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_about -> {
                MaterialAlertDialogBuilder(this)
                    .setTitle("Acerca de CinefilosFC")
                    .setMessage("Versión 1.0\nDesarrollado para la clase de Programación de Dispositivos Móviles de la Facultad de Ciencias UNAM.\n\nDatos proporcionados por la API de TMDB.")
                    .setPositiveButton("Aceptar") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
                true
            }
            R.id.action_share -> {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "¡Mira la app de películas que estoy haciendo!.")
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, "Compartir app vía...")
                startActivity(shareIntent)
                true
            }
            R.id.action_support -> {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = android.net.Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL, arrayOf("lm.solares@ciencias.unam.mx"))
                    putExtra(Intent.EXTRA_SUBJECT, "Soporte App CinefilosFC")
                    putExtra(Intent.EXTRA_TEXT, "Hola, tengo una duda/reporte sobre la aplicación...")
                }
                startActivity(Intent.createChooser(intent, "Enviar correo de soporte..."))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }


    }

    private fun configurarAdaptador(movies: List<Movie>) {
        val rvMovies = findViewById<RecyclerView>(R.id.rv_movies)
        rvMovies.adapter = MovieAdapter(movies) { movieSeleccionada ->
            val intent = Intent(this, NewReviewActivity::class.java)
            intent.putExtra("MOVIE_TITLE", movieSeleccionada.title)
            intent.putExtra("MOVIE_POSTER", movieSeleccionada.posterPath)
            startActivity(intent)
        }
    }

}


