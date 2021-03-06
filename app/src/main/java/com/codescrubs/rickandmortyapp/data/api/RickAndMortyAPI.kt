package com.codescrubs.rickandmortyapp.data.api

import com.codescrubs.rickandmortyapp.data.api.response.PaginatedResult
import com.codescrubs.rickandmortyapp.domain.Character
import com.codescrubs.rickandmortyapp.domain.Location
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface RickAndMortyAPI {
    @GET("character")
    fun getPaginatedCharactersAsync(): Deferred<Response<PaginatedResult<Character>>>

    @GET
    fun getPaginatedCharactersAsync(@Url url: String): Deferred<Response<PaginatedResult<Character>>>

    @GET("character/{id}")
    fun getCharacterAsync(@Path("id") id: String): Deferred<Response<Character>>

    @GET("character/{ids}")
    fun getCharactersAsync(@Path("ids") ids: String): Deferred<Response<List<Character>>>

    @GET
    fun getLocationAsync(@Url url: String): Deferred<Response<Location>>
}