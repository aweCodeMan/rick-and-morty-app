package com.codescrubs.rickandmortyapp.ui.activities

import com.codescrubs.rickandmortyapp.data.api.RickAndMortyClient
import com.codescrubs.rickandmortyapp.domain.Character
import com.codescrubs.rickandmortyapp.domain.Location
import com.codescrubs.rickandmortyapp.mvp.LocationResidentsMVP
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class LocationResidentsPresenter(private val view: LocationResidentsMVP.View, private val location: Location) : LocationResidentsMVP.Presenter, CoroutineScope {
    private val job = Job()

    override val coroutineContext: CoroutineContext = job + Dispatchers.IO

    override fun onStart() {
        view.showProgress()

        launch {
            //  TODO: Refactor this into a standalone data provider class
            val ids = location.residents.map { it.replace("https://rickandmortyapi.com/api/character/", "") }
            val result = RickAndMortyClient.rickAndMortyAPI.getCharactersByIdsAsync(ids.joinToString(separator = ",")).await()

            withContext(Dispatchers.Main) {
                view.showResidents(result)
                view.hideProgress()
            }
        }
    }

    override fun onDestroy() {
        job.cancel()
    }

    override fun onResidentClicked(character: Character) {
        view.showCharacterDetail(character)
    }
}

