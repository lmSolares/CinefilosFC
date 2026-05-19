package com.pdm.cinefilosfc

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    private lateinit var dbHelper: CinefilosDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dbHelper = CinefilosDB(this)

        val editUsername = findViewById<TextInputEditText>(R.id.edit_login_username)
        val editPassword = findViewById<TextInputEditText>(R.id.edit_login_password)
        val btnLogin = findViewById<Button>(R.id.btn_login_enter)

        btnLogin.setOnClickListener {
            val username = editUsername.text.toString().trim()

            if (username.isNotEmpty()) {
                dbHelper.verificarOInsertarUsuario(username)
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra("LOGGED_IN_USERNAME", username)
                }
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Por favor ingresa tu usuario", Toast.LENGTH_SHORT).show()
            }
        }
    }
}