package com.example.pokemonapp.network

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pokemonapp.dataclasses.Pokemon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

class PokemonPagingSource<T : Any>(
    private val apiService: ApiService,
) : PagingSource<Int, T>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        return withContext(Dispatchers.IO) {
            try {
                val nextOffSetNumber = params.key ?: 0
                val response = apiService.getPokemons(nextOffSetNumber)

                Log.i("response", apiService.getPokemons(nextOffSetNumber).body().toString())


                return@withContext if (response.isSuccessful) {
                    var items = response.body()?.let { body ->
                        when (body) {
                            is PokemonResponse -> body.results
                            else -> emptyList()
                        }
                    } ?: emptyList()
                    Log.i("pokeList1", items.toString())


                    val deferredList = items.map { item ->
                        async {
                            if (item is Pokemon) {
                                val temp = item.url.split("/")
                                val id = temp[temp.size - 2].toInt()
                                apiService.getPokemon(id).body()
                            } else {
                                null
                            }
                        }
                    }

                    val pokemonList = deferredList.awaitAll().filterNotNull()

                    items = pokemonList
                    Log.i("pokeList", pokemonList.toString())
                    Log.i("pokeList", items.toString())

                    LoadResult.Page(
                        data = items as List<T>,
                        prevKey = if (nextOffSetNumber == 0) null else nextOffSetNumber - 20,
                        nextKey = if (items.isEmpty()) null else nextOffSetNumber + 20
                    )
                } else {
                    LoadResult.Error(Exception("Error fetching data"))
                }
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition
    }
}
