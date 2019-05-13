package com.codescrubs.rickandmortyapp.data.api

import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.ArraySet
import android.util.Log
import com.codescrubs.rickandmortyapp.data.api.response.PaginatedResult
import com.codescrubs.rickandmortyapp.domain.Character
import com.codescrubs.rickandmortyapp.domain.Location
import com.codescrubs.rickandmortyapp.ui.App

class DataSource(private val client: RickAndMortyAPI = RickAndMortyClient.rickAndMortyAPI) {

    private val preferences: SharedPreferences by lazy { PreferenceManager.getDefaultSharedPreferences(App.instance) }

    companion object {
        private const val FAVORITE_CHARACTERS = "favorites"
    }

    suspend fun getPaginatedCharacters(): PaginatedResult<Character> {
        val result = client.getPaginatedCharactersAsync().await()
        return PaginatedResult(result.info, handleFavorite(result.results))
    }

    suspend fun getFavoriteCharacters(): List<Character> {
        val favorites = preferences.getStringSet(FAVORITE_CHARACTERS, ArraySet())!!.toList()

        return when (favorites.size) {
            0 -> listOf()
            1 -> listOf(handleFavorite(client.getCharacterAsync(favorites[0]).await()))
            else -> handleFavorite(client.getCharactersAsync(favorites.joinToString(separator = ",")).await())
        }
    }

    suspend fun getCharactersByURLs(characterURLs: List<String>): List<Character> {
        //  TODO: Use regex to extract the ID of the character from the URL
        val ids = characterURLs.map { it.replace("https://rickandmortyapi.com/api/character/", "") }
        return handleFavorite(client.getCharactersAsync(ids.joinToString(separator = ",")).await())
    }

    //  TODO: It would be better to pass in PaginatedResult and handle the next page url logic here
    suspend fun getNextPageOfPaginatedCharacters(pageUrl: String): PaginatedResult<Character> {
        val result = client.getPaginatedCharactersAsync(pageUrl).await()
        return PaginatedResult(result.info, handleFavorite(result.results))
    }

    suspend fun getLocation(url: String): Location {
        return client.getLocationAsync(url).await()
    }

    fun favorCharacter(character: Character): Character {
        val favorites = HashSet(preferences.getStringSet(FAVORITE_CHARACTERS, HashSet()))
        favorites.add(character.id.toString())

        preferences.edit()
            .putStringSet(FAVORITE_CHARACTERS, favorites)
            .apply()

        return character.copy(isFavorite = true)
    }

    fun unfavorCharacter(character: Character): Character {
        val favorites = HashSet(preferences.getStringSet(FAVORITE_CHARACTERS, HashSet()))
        favorites.remove(character.id.toString())

        preferences.edit()
            .putStringSet(FAVORITE_CHARACTERS, favorites)
            .apply()

        return character.copy(isFavorite = false)
    }

    private fun handleFavorite(characters: List<Character>): List<Character> {
        return characters.map { handleFavorite(it) }
    }

    private fun handleFavorite(character: Character): Character {
        return character.copy(isFavorite = isFavorite(character))
    }

    private fun isFavorite(character: Character): Boolean {
        val favorites = preferences.getStringSet(FAVORITE_CHARACTERS, HashSet())
        return favorites!!.contains(character.id.toString())
    }
}