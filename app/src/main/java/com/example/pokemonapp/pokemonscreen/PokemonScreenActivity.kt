package com.example.pokemonapp.pokemonscreen

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.pokemonapp.R
import com.example.pokemonapp.RoundedCornerTransformation
import com.example.pokemonapp.network.ApiService
import com.example.pokemonapp.network.PokemonRepository
import com.example.pokemonapp.network.PokemonViewModelFactory
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.util.Locale


class PokemonScreenActivity : AppCompatActivity() {
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            PokemonViewModelFactory(
                PokemonRepository(ApiService.create()),
                intent.getIntExtra("EXTRA_ID", -1)
            )
        )
            .get(PokemonScreenViewModel::class.java)
    }
    private lateinit var topAppBar: MaterialToolbar
    private lateinit var progressIndicator: LinearProgressIndicator
    private lateinit var pokemonImage: ImageView
    private lateinit var pokemonName: TextView
    private lateinit var pokemonAbilities: TextView
    private lateinit var pokemonHeight: TextView
    private lateinit var pokemonWeight: TextView
    private lateinit var hp: TextView
    private lateinit var attack: TextView
    private lateinit var defense: TextView
    private lateinit var speed: TextView
    private lateinit var specialAttack: TextView
    private lateinit var specialDefense: TextView
    private lateinit var pokemonProgress: CircularProgressIndicator


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_screen)
        topAppBar = findViewById(R.id.topAppBar)
        progressIndicator = findViewById(R.id.progressIndicator)
        pokemonImage = findViewById(R.id.pokemonImage)
        pokemonName = findViewById(R.id.pokemonName)
        pokemonAbilities = findViewById(R.id.abilities)
        pokemonHeight = findViewById(R.id.height)
        pokemonWeight = findViewById(R.id.weight)
        hp = findViewById(R.id.hp)
        attack = findViewById(R.id.attack)
        defense = findViewById(R.id.defense)
        speed = findViewById(R.id.speed)
        specialAttack = findViewById(R.id.special_attack)
        specialDefense = findViewById(R.id.special_defense)
        pokemonProgress = findViewById(R.id.pokemonProgressImage)


        viewModel.pokemon.observe(this) { pokemon ->
            with(pokemon ?: return@observe) {
                progressIndicator.visibility = View.GONE

                pokemonName.text =
                    name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                pokemonHeight.text = buildString {
                    append(getString(R.string.height))
                    append(" ")
                    append(height)
                }
                pokemonWeight.text = buildString {
                    append(getString(R.string.weight))
                    append(" ")
                    append(weight)
                }


                topAppBar.title =
                    name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                Picasso.get()
                    .load(sprites.other.`official-artwork`.front_default)
                    .resize(400, 400)
                    .transform(RoundedCornerTransformation())
                    .into(pokemonImage,
                        object : Callback {
                            override fun onSuccess() {
                                pokemonProgress.visibility = View.GONE
                            }

                            override fun onError(e: Exception?) {
                                pokemonProgress.visibility = View.VISIBLE
                            }
                        }
                    )

                pokemonAbilities.text = buildString {
                    append(getString(R.string.abilities))
                    append(" ")
                    append(abilities.joinToString { it.ability.name })
                }

                stats.forEach { stat ->
                    when (stat.stat.name) {
                        "hp" -> hp.text = buildString {
                            append(getString(R.string.hp))
                            append(" ")
                            append(stat.base_stat)
                        }

                        "attack" -> attack.text = buildString {
                            append(getString(R.string.attack))
                            append(" ")
                            append(stat.base_stat)
                        }

                        "defense" -> defense.text = buildString {
                            append(getString(R.string.defense))
                            append(" ")
                            append(stat.base_stat)
                        }

                        "speed" -> speed.text = buildString {
                            append(getString(R.string.speed))
                            append(" ")
                            append(stat.base_stat)
                        }

                        "special-attack" -> specialAttack.text = buildString {
                            append(getString(R.string.special_attack))
                            append(" ")
                            append(stat.base_stat)
                        }

                        "special-defense" -> specialDefense.text =
                            buildString {
                                append(getString(R.string.special_defense))
                                append(" ")
                                append(stat.base_stat)
                            }
                    }
                }
            }
        }
        topAppBar.setNavigationOnClickListener {
            finish()
        }
    }
}