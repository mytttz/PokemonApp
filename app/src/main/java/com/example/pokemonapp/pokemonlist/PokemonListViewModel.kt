package com.example.pokemonapp.pokemonlist

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.pokemonapp.dataclasses.Pokemon
import com.example.pokemonapp.network.PokemonRepository
import com.example.pokemonapp.pokemonscreen.PokemonScreenActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class PokemonListViewModel(
    private val repository: PokemonRepository,
) : ViewModel() {

    private val _pokemons = MutableLiveData<PagingData<Pokemon>>()
    val pokemons: LiveData<PagingData<Pokemon>> get() = _pokemons

    private val _error = MutableLiveData<String>()


    val error: LiveData<String> get() = _error

    init {
        fetchPokemons()
    }

    fun fetchPokemons() {
        val pagingSource = repository.getPokemonsPagingSource()
        val pager = Pager(PagingConfig(pageSize = 20)) {
            pagingSource
        }
        viewModelScope.launch {
            pager.flow.cachedIn(viewModelScope).collectLatest {
                _pokemons.postValue(it)
            }
        }
    }

    fun selectedPokemon(context: Context, id: Int?) {
        val intentPokemonScreen = Intent(context, PokemonScreenActivity::class.java)
        intentPokemonScreen.putExtra("EXTRA_ID", id)
        context.startActivity(intentPokemonScreen)
    }
}

