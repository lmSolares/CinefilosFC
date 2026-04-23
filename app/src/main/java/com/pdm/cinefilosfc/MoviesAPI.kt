package com.pdm.cinefilosfc

import com.pdm.cinefilosfc.models.TmdbResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("3/search/multi")
    suspend fun searchMulti(
        @Query("query") query: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "es-MX"
    ): TmdbResponse

    @GET("3/movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "es-MX"
    ): TmdbResponse

    @GET("3/tv/popular")
    suspend fun getPopularTVShows(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "es-MX"
    ): TmdbResponse

    // Para filtrar por géneros (Terror, Romance, Acción)
    @GET("3/discover/movie")
    suspend fun getMoviesByGenre(
        @Query("api_key") apiKey: String,
        @Query("with_genres") genreId: String,
        @Query("language") language: String = "es-MX"
    ): TmdbResponse

}

object RetrofitClient {
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}