package com.codescrubs.rickandmortyapp.data.api

import com.codescrubs.rickandmortyapp.data.api.response.PaginatedResult
import com.codescrubs.rickandmortyapp.domain.Character
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface RickAndMortyAPI {
    @GET("character")
    fun getCharactersAsync(): Deferred<PaginatedResult<Character>>
}