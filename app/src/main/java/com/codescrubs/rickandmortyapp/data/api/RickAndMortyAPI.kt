package com.codescrubs.rickandmortyapp.data.api

import com.codescrubs.rickandmortyapp.data.api.response.PaginatedResult
import com.codescrubs.rickandmortyapp.domain.Character
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Url

interface RickAndMortyAPI {
    @GET("character")
    fun getCharactersAsync(): Deferred<PaginatedResult<Character>>

    @GET
    fun getNextCharactersPageAsync(@Url url: String): Deferred<PaginatedResult<Character>>
}