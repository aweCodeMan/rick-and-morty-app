package com.codescrubs.rickandmortyapp.data.api

import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.ArraySet
import com.codescrubs.rickandmortyapp.data.api.response.PaginatedResult
import com.codescrubs.rickandmortyapp.data.api.response.Result
import com.codescrubs.rickandmortyapp.domain.Character
import com.codescrubs.rickandmortyapp.domain.Location
import com.codescrubs.rickandmortyapp.ui.App
import java.io.IOException

//  TODO: Refactor this into Character/Location repository with a dedicated API server handler
class DataSource(private val client: RickAndMortyAPI = RickAndMortyClient.rickAndMortyAPI) {

    private val preferences: SharedPreferences by lazy { PreferenceManager.getDefaultSharedPreferences(App.instance) }

    companion object {
        private const val FAVORITE_CHARACTERS = "favorites"
    }

    suspend fun getPaginatedCharacters(): Result<PaginatedResult<Character>> {
        try {
            val response = client.getPaginatedCharactersAsync().await()

            if (!response.isSuccessful) {
                return Result.Error(IOException(response.errorBody().toString()))
            }

            val body = response.body()!!
            return Result.Success(PaginatedResult(body.info, handleFavorite(body.results)))
        } catch (exception: Exception) {
            return Result.Error(IOException(exception.message))
        }
    }

    //  TODO: Abstract the logic of handling server response (single character returns an object, otherwise a list)
    suspend fun getFavoriteCharacters(): Result<List<Character>> {
        try {
            val favorites = preferences.getStringSet(FAVORITE_CHARACTERS, ArraySet())!!.toList()

            if (favorites.isEmpty()) {
                return Result.Success(listOf())
            }

            val response = when (favorites.size) {
                1 -> client.getCharacterAsync(favorites[0]).await()
                else -> client.getCharactersAsync(favorites.joinToString(separator = ",")).await()
            }

            if (!response.isSuccessful) {
                return Result.Error(IOException(response.errorBody().toString()))
            }

            return when (favorites.size) {
                1 -> Result.Success(listOf(handleFavorite(response.body()!! as Character)))
                else -> Result.Success(handleFavorite(response.body()!! as List<Character>))
            }
        } catch (exception: Exception) {
            return Result.Error(IOException(exception.message))
        }
    }

    suspend fun getCharactersByURLs(characterURLs: List<String>): Result<List<Character>> {
        try {
            //  TODO: Use regex to extract the ID of the character from the URL
            val ids = characterURLs.map { it.replace("https://rickandmortyapi.com/api/character/", "") }

            val response = client.getCharactersAsync(ids.joinToString(separator = ",")).await()

            if (!response.isSuccessful) {
                return Result.Error(IOException(response.errorBody().toString()))
            }

            return Result.Success(handleFavorite(response.body()!!))
        } catch (exception: Exception) {
            return Result.Error(IOException(exception.message))
        }
    }

    //  TODO: It would be better to pass in PaginatedResult and handle the next page url logic here
    suspend fun getNextPageOfPaginatedCharacters(pageUrl: String): Result<PaginatedResult<Character>> {
        try {

            val response = client.getPaginatedCharactersAsync(pageUrl).await()

            if (!response.isSuccessful) {
                return Result.Error(IOException(response.errorBody().toString()))
            }

            val body = response.body()!!
            return Result.Success(PaginatedResult(body.info, handleFavorite(body.results)))
        } catch (exception: Exception) {
            return Result.Error(IOException(exception.message))
        }
    }

    suspend fun getLocation(url: String): Result<Location> {
        try {

            val response = client.getLocationAsync(url).await()

            if (!response.isSuccessful) {
                return Result.Error(IOException(response.errorBody().toString()))
            }

            return Result.Success(response.body()!!)

        } catch (exception: Exception) {
            return Result.Error(IOException(exception.message))
        }
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

    fun numberOfFavorites(): Int {
        val favorites = HashSet(preferences.getStringSet(FAVORITE_CHARACTERS, HashSet()))
        return favorites.size
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