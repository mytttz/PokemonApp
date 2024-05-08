package com.example.pokemonapp.dataclasses

data class Pokemon(
    val url: String,
    val id: Int,
    val name: String,
    val weight: String,
    val height: String,
    val base_experience: String,
    val sprites: Sprites,
    val stats: List<Stat>,
    val abilities: List<Abilities>,
)

data class Sprites(
    val front_default: String,
    val front_shiny: String,
    val other: Other
)

data class Other(
    val `official-artwork`: Artwork,

    )

data class Artwork(
    val front_default: String,
)

data class Stat(
    val base_stat: String,
    val stat: StatName,
)

data class StatName(
    val name: String,
)

data class Abilities(
    val ability: Ability,
)

data class Ability(
    val name: String
)