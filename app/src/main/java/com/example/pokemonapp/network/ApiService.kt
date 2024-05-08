package com.example.pokemonapp.network

import com.example.pokemonapp.dataclasses.Pokemon
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("pokemon")
    suspend fun getPokemons(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int = 20,
    ): Response<PokemonResponse>


    @GET("pokemon/{id}")
    suspend fun getPokemon(
        @Path("id") id: Int,
    ): Response<Pokemon>


    companion object {
        fun create(): ApiService {
            return Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}