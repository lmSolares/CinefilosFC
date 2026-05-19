package com.pdm.cinefilosfc

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class PerfilActivity : AppCompatActivity() {

    private lateinit var tvUsernameTitle: TextView
    private lateinit var tvDisplayName: TextView
    private lateinit var tvDisplayGenre: TextView
    private lateinit var tvDisplayCountry: TextView
    private lateinit var tvDisplaySex: TextView
    private lateinit var dbHelper: CinefilosDB
    private var currentUser: String = "admin"

    private val startEditProfileForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            cargarDatosDesdeBaseDeDatos()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.perfil)

        dbHelper = CinefilosDB(this)

        intent.getStringExtra("LOGGED_IN_USERNAME")?.let {
            currentUser = it
        }

        tvUsernameTitle = findViewById(R.id.tv_username_title)
        tvDisplayName = findViewById(R.id.tv_display_name)
        tvDisplayGenre = findViewById(R.id.tv_display_genre)
        tvDisplayCountry = findViewById(R.id.tv_display_country)
        tvDisplaySex = findViewById(R.id.tv_display_sex)
        val btnEditProfile = findViewById<Button>(R.id.btn_edit_profile)

        cargarDatosDesdeBaseDeDatos()

        val bottomNavigationView = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.perfil
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, MainActivity::class.java).apply {
                        putExtra("LOGGED_IN_USERNAME", currentUser)
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    }
                    startActivity(intent)
                    true
                }
                R.id.perfil -> true
                else -> false
            }
        }

        btnEditProfile.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java).apply {
                putExtra("CURRENT_USERNAME", currentUser)
                putExtra("CURRENT_NAME", tvDisplayName.text.toString())
                putExtra("CURRENT_GENRE", tvDisplayGenre.text.toString())
                putExtra("CURRENT_COUNTRY", tvDisplayCountry.text.toString())
                putExtra("CURRENT_SEX", tvDisplaySex.text.toString())
            }
            startEditProfileForResult.launch(intent)
        }
    }

    private fun cargarDatosDesdeBaseDeDatos() {
        val datos = dbHelper.obtenerUsuario(currentUser)
        datos?.let {
            tvUsernameTitle.text = "@${it[CinefilosDB.COLUMN_USERNAME]}"

            val nombre = it[CinefilosDB.COLUMN_NOMBRE]
            tvDisplayName.text = if (!nombre.isNullOrEmpty()) nombre else "No configurado"

            val genero = it[CinefilosDB.COLUMN_GENERO]
            tvDisplayGenre.text = if (!genero.isNullOrEmpty()) genero else "No configurado"

            val pais = it[CinefilosDB.COLUMN_PAIS]
            tvDisplayCountry.text = if (!pais.isNullOrEmpty()) pais else "No configurado"

            val sexo = it[CinefilosDB.COLUMN_SEXO]
            tvDisplaySex.text = if (!sexo.isNullOrEmpty()) sexo else "No especificado"
        }
    }
}