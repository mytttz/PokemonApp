package com.example.pokemonapp.network

import com.example.pokemonapp.dataclasses.Pokemon

data class PokemonResponse(
    val results: List<Pokemon>
)
