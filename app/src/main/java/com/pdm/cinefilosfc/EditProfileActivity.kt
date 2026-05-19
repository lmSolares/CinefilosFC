package com.pdm.cinefilosfc

import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class EditProfileActivity : AppCompatActivity() {

    private lateinit var dbHelper: CinefilosDB
    private lateinit var usernameKey: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        dbHelper = CinefilosDB(this)

        val editUsername = findViewById<TextInputEditText>(R.id.edit_username)
        val editName = findViewById<TextInputEditText>(R.id.edit_name)
        val editGenre = findViewById<TextInputEditText>(R.id.edit_favorite_genre)
        val editCountry = findViewById<TextInputEditText>(R.id.edit_pais)
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroupEdit)
        val btnSave = findViewById<Button>(R.id.btn_save_profile)
        val btnBack = findViewById<TextView>(R.id.btn_back_to_profile)

        usernameKey = intent.getStringExtra("CURRENT_USERNAME") ?: ""
        editUsername.setText(usernameKey)
        editUsername.isEnabled = false

        editName.setText((intent.getStringExtra("CURRENT_NAME") ?: "").replace("No configurado", ""))
        editGenre.setText((intent.getStringExtra("CURRENT_GENRE") ?: "").replace("No configurado", ""))
        editCountry.setText((intent.getStringExtra("CURRENT_COUNTRY") ?: "").replace("No configurado", ""))

        when (intent.getStringExtra("CURRENT_SEX")) {
            "Femenino" -> findViewById<RadioButton>(R.id.edit_fem).isChecked = true
            "Masculino" -> findViewById<RadioButton>(R.id.edit_masc).isChecked = true
            "Otro" -> findViewById<RadioButton>(R.id.edit_otro).isChecked = true
        }

        btnBack.setOnClickListener { finish() }

        btnSave.setOnClickListener {
            val selectedRadioId = radioGroup.checkedRadioButtonId
            val selectedRadio = findViewById<RadioButton>(selectedRadioId)
            val sexText = selectedRadio?.text?.toString() ?: "No especificado"

            val exito = dbHelper.actualizarPerfil(
                usernameKey,
                editName.text.toString().trim(),
                editGenre.text.toString().trim(),
                editCountry.text.toString().trim(),
                sexText
            )

            if (exito) {
                Toast.makeText(this, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
            } else {
                Toast.makeText(this, "Error al actualizar en la base de datos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}