package com.codescrubs.rickandmortyapp.data.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RickAndMortyClient
{
    private fun retrofit(client: OkHttpClient = OkHttpProvider.instance) : Retrofit = Retrofit.Builder()
        .baseUrl("https://rickandmortyapi.com/api/")
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val rickAndMortyAPI : RickAndMortyAPI = retrofit().create(RickAndMortyAPI::class.java)
}
