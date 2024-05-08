package com.example.pokemonapp.pokemonlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemonapp.R
import com.example.pokemonapp.RoundedCornerTransformation
import com.example.pokemonapp.dataclasses.Pokemon
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso


class PokemonAdapter(
    private val context: Context,
    private val viewModel: PokemonListViewModel
) :
    PagingDataAdapter<Pokemon, PokemonAdapter.PokemonViewHolder>(PokemonDiffCallback()) {

    inner class PokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pokemonName: TextView = itemView.findViewById(R.id.pokemonName)
        val pokemonImage: ImageView = itemView.findViewById(R.id.pokemonImage)
        val pokemonProgress: CircularProgressIndicator = itemView.findViewById(R.id.pokemonProgress)


        init {
            itemView.setOnClickListener {
                viewModel.selectedPokemon(context, getItem(absoluteAdapterPosition)?.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.pokemon_item, parent, false)
        return PokemonViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.pokemonProgress.visibility = View.VISIBLE
        holder.pokemonName.text =
            getItem(position)?.name?.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        Picasso.get()
            .load(getItem(position)?.sprites?.other?.`official-artwork`?.front_default)
            .transform(RoundedCornerTransformation())
            .into(holder.pokemonImage,
                object : Callback {
                    override fun onSuccess() {
                        holder.pokemonProgress.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {
                        holder.pokemonProgress.visibility = View.VISIBLE
                    }
                }
            )
    }


    class PokemonDiffCallback : DiffUtil.ItemCallback<Pokemon>() {
        override fun areItemsTheSame(
            oldItem: Pokemon,
            newItem: Pokemon
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Pokemon,
            newItem: Pokemon
        ): Boolean {
            return oldItem == newItem
        }
    }
}