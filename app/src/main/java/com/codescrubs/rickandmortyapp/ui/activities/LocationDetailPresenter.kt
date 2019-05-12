package com.codescrubs.rickandmortyapp.ui.activities

import com.codescrubs.rickandmortyapp.data.api.RickAndMortyClient
import com.codescrubs.rickandmortyapp.domain.PartialLocation
import com.codescrubs.rickandmortyapp.mvp.LocationDetailMVP
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class LocationDetailPresenter(private val view: LocationDetailMVP.View, private val partialLocation: PartialLocation) : LocationDetailMVP.Presenter, CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext = job + Dispatchers.IO

    override fun onStart() {
        view.showProgress()

        launch {
            val result = RickAndMortyClient.rickAndMortyAPI.getLocationAsync(partialLocation.url).await()

            withContext(Dispatchers.Main) {
                view.showLocation(result)
                view.hideProgress()
            }
        }
    }

    override fun onDestroy() {
        job.cancel()
    }

    override fun onShowResidentsClicked() {
        view.showResidents()
    }
}

