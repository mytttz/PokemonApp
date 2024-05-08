package com.example.pokemonapp.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pokemonapp.pokemonlist.PokemonListViewModel
import com.example.pokemonapp.pokemonscreen.PokemonScreenViewModel


class PokemonViewModelFactory(
    private val repository: PokemonRepository,
    private val additionalIntValue: Int? = null
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PokemonListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PokemonListViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(PokemonScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PokemonScreenViewModel(repository, additionalIntValue ?: 0) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

