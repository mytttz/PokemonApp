package com.example.pokemonapp.pokemonlist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemonapp.R
import com.example.pokemonapp.network.ApiService
import com.example.pokemonapp.network.PokemonRepository
import com.example.pokemonapp.network.PokemonViewModelFactory


class PokemonListActivity : AppCompatActivity() {

    private lateinit var pokemonRecycler: RecyclerView

    private lateinit var viewModel: PokemonListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_list)
        pokemonRecycler = findViewById(R.id.pokemonList)
        viewModel =
            ViewModelProvider(
                this,
                PokemonViewModelFactory(PokemonRepository(ApiService.create()))
            )[PokemonListViewModel::class.java]

        val adapter = PokemonAdapter(this, viewModel)

        pokemonRecycler.adapter = adapter
        pokemonRecycler.layoutManager = LinearLayoutManager(this)

        viewModel.pokemons.observe(this) { pagingData ->
            adapter.submitData(lifecycle, pagingData)
        }
    }
}