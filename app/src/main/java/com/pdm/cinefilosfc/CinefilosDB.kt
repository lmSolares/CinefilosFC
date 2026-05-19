package com.pdm.cinefilosfc

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.pdm.cinefilosfc.models.Review

class CinefilosDB(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "CinefilosFC.db"
        private const val DATABASE_VERSION = 3

        const val TABLE_USUARIOS = "usuarios"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_NOMBRE = "nombre"
        const val COLUMN_GENERO = "genero_favorito"
        const val COLUMN_PAIS = "pais"
        const val COLUMN_SEXO = "sexo"

        const val TABLE_RESENAS = "resenas"
        const val COLUMN_ID_RESENA = "id_resena"
        const val COLUMN_RESENA_USER = "username_fk"
        const val COLUMN_MOVIE_TITLE = "movie_title"
        const val COLUMN_REVIEW_TEXT = "review_text"
        const val COLUMN_RATING = "rating"
        const val COLUMN_POSTER = "poster_path"
        const val COLUMN_YEAR = "release_year"
        const val COLUMN_FECHA = "fecha"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableStatement = """
            CREATE TABLE $TABLE_USUARIOS (
                $COLUMN_USERNAME TEXT PRIMARY KEY,
                $COLUMN_NOMBRE TEXT,
                $COLUMN_GENERO TEXT,
                $COLUMN_PAIS TEXT,
                $COLUMN_SEXO TEXT
            )
        """.trimIndent()
        db.execSQL(createTableStatement)

        val createTableResenas = """
            CREATE TABLE $TABLE_RESENAS (
                $COLUMN_ID_RESENA INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_RESENA_USER TEXT,
                $COLUMN_MOVIE_TITLE TEXT,
                $COLUMN_REVIEW_TEXT TEXT,
                $COLUMN_RATING REAL,
                $COLUMN_POSTER TEXT,
                $COLUMN_YEAR TEXT,
                $COLUMN_FECHA TEXT
            )
        """.trimIndent()
        db.execSQL(createTableResenas)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USUARIOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_RESENAS")
        onCreate(db)
    }

    fun verificarOInsertarUsuario(username: String) {
        val db = this.writableDatabase
        val cursor = db.query(
            TABLE_USUARIOS,
            arrayOf(COLUMN_USERNAME),
            "$COLUMN_USERNAME = ?",
            arrayOf(username),
            null, null, null
        )

        if (cursor.count == 0) {
            val values = ContentValues().apply {
                put(COLUMN_USERNAME, username)
                put(COLUMN_NOMBRE, "")
                put(COLUMN_GENERO, "")
                put(COLUMN_PAIS, "")
                put(COLUMN_SEXO, "No especificado")
            }
            db.insert(TABLE_USUARIOS, null, values)
        }
        cursor.close()
        db.close()
    }

    fun actualizarPerfil(username: String, nombre: String, genero: String, pais: String, sexo: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE, nombre)
            put(COLUMN_GENERO, genero)
            put(COLUMN_PAIS, pais)
            put(COLUMN_SEXO, sexo)
        }

        val filasAfectadas = db.update(TABLE_USUARIOS, values, "$COLUMN_USERNAME = ?", arrayOf(username))
        db.close()
        return filasAfectadas > 0
    }

    fun obtenerUsuario(username: String): HashMap<String, String>? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_USUARIOS,
            null,
            "$COLUMN_USERNAME = ?",
            arrayOf(username),
            null, null, null
        )

        var usuarioData: HashMap<String, String>? = null

        if (cursor.moveToFirst()) {
            usuarioData = HashMap()
            usuarioData[COLUMN_USERNAME] = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME))
            usuarioData[COLUMN_NOMBRE] = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE))
            usuarioData[COLUMN_GENERO] = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENERO))
            usuarioData[COLUMN_PAIS] = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PAIS))
            usuarioData[COLUMN_SEXO] = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SEXO))
        }
        cursor.close()
        db.close()
        return usuarioData
    }

    fun insertarResena(username: String, movieTitle: String, reviewText: String, rating: Float, posterPath: String?, year: String, fecha: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_RESENA_USER, username)
            put(COLUMN_MOVIE_TITLE, movieTitle)
            put(COLUMN_REVIEW_TEXT, reviewText)
            put(COLUMN_RATING, rating)
            put(COLUMN_POSTER, posterPath ?: "")
            put(COLUMN_YEAR, year)
            put(COLUMN_FECHA, fecha)
        }
        val result = db.insert(TABLE_RESENAS, null, values)
        db.close()
        return result != -1L
    }

    fun obtenerResenasPorUsuario(username: String): List<Review> {
        val listaResenas = mutableListOf<Review>()
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_RESENAS,
            null,
            "$COLUMN_RESENA_USER = ?",
            arrayOf(username),
            null, null,
            "$COLUMN_ID_RESENA DESC"
        )

        if (cursor.moveToFirst()) {
            do {
                val review = Review(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID_RESENA)),
                    movieTitle = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MOVIE_TITLE)),
                    reviewText = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REVIEW_TEXT)),
                    rating = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_RATING)),
                    posterPath = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POSTER)),
                    releaseYear = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_YEAR)),
                    fecha = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA))
                )
                listaResenas.add(review)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return listaResenas
    }
}