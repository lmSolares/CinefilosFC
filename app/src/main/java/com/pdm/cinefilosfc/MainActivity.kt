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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
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

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.selectedItemId = R.id.nav_home
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_search -> true
                R.id.nav_profile -> {
                    startActivity(Intent(this, PerfilActivity::class.java))
                    true
                }
                R.id.nav_settings -> true
                else -> false
            }
        }

        val rvMovies = findViewById<RecyclerView>(R.id.rv_movies)
        rvMovies.layoutManager = GridLayoutManager(this, 2)

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val myApiKey = "ccdc150cc34be143bce60d00f3de897b"

                val response = RetrofitClient.api.getPopularMovies(myApiKey)

                withContext(Dispatchers.Main) {
                    val adapter = MovieAdapter(response.results)
                    rvMovies.adapter = adapter
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
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                true
            }
            R.id.action_search -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}


