package com.pdm.cinefilosfc

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pdm.cinefilosfc.models.Review

class MyReviewsActivity : AppCompatActivity() {

    private lateinit var db: CinefilosDB
    private lateinit var rvResenas: RecyclerView
    private lateinit var adapter: ReviewAdapter
    private var currentUser: String = "admin"
    private var todasLasResenas: List<Review> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_reviews_activity)

        db = CinefilosDB(this)
        currentUser = intent.getStringExtra("LOGGED_IN_USERNAME") ?: "admin"

        rvResenas = findViewById(R.id.rv_mis_resenas)
        rvResenas.layoutManager = LinearLayoutManager(this)

        todasLasResenas = db.obtenerResenasPorUsuario(currentUser)
        adapter = ReviewAdapter(todasLasResenas)
        rvResenas.adapter = adapter

        configurarFiltro()
        configurarNavegacion()
    }

    private fun configurarFiltro() {
        val spinner = findViewById<Spinner>(R.id.spinner_filter)
        val opciones = arrayOf("Todas", "5 estrellas", "4 estrellas", "3 estrellas", "2 estrellas", "1 estrella")

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, opciones)
        spinner.adapter = arrayAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

                val filtro = when (position) {
                    1 -> 5f
                    2 -> 4f
                    3 -> 3f
                    4 -> 2f
                    5 -> 1f
                    else -> -1f
                }

                val listaFiltrada = if (filtro == -1f) {
                    todasLasResenas
                } else {
                    todasLasResenas.filter { it.rating >= filtro && it.rating < filtro + 1 }
                }

                adapter.updateData(listaFiltrada)
                (parent.getChildAt(0) as? TextView)?.setTextColor(Color.WHITE)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun configurarNavegacion() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.selectedItemId = R.id.nav_mis_resenas

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, MainActivity::class.java).apply {
                        putExtra("LOGGED_IN_USERNAME", currentUser)
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    }
                    startActivity(intent)
                    true
                }
                R.id.nav_mis_resenas -> true
                R.id.nav_profile -> {
                    val intent = Intent(this, PerfilActivity::class.java).apply {
                        putExtra("LOGGED_IN_USERNAME", currentUser)
                    }
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }
}