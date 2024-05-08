package com.example.pokemonapp.pokemonscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonapp.dataclasses.Pokemon
import com.example.pokemonapp.network.PokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class PokemonScreenViewModel(
    private val repository: PokemonRepository,
    request: Int?
) : ViewModel() {

    private val _pokemon = MutableLiveData<Pokemon>()
    val pokemon: LiveData<Pokemon> get() = _pokemon


    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    init {
        request?.let {
            fetchPokemon(it)
        }
    }

    private fun fetchPokemon(id: Int) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val response = repository.getPokemon(id)
                    if (response.isSuccessful) {
                        _pokemon.postValue(response.body())
                    } else {
                        _error.postValue("Failed to fetch movie: ${response.message()}")
                    }
                }
            } catch (e: Exception) {
                _error.postValue("Error occurred: ${e.message}")
            }
        }
    }
}