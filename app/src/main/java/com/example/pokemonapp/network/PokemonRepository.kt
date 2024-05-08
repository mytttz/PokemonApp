package com.example.pokemonapp.network

import androidx.paging.PagingSource
import com.example.pokemonapp.dataclasses.Pokemon


import retrofit2.Response

class PokemonRepository(private val apiService: ApiService) {


    suspend fun getPokemon(id: Int): Response<Pokemon> {
        return apiService.getPokemon(id)
    }

    fun getPokemonsPagingSource(): PagingSource<Int, Pokemon> {
        return PokemonPagingSource(apiService)
    }

}