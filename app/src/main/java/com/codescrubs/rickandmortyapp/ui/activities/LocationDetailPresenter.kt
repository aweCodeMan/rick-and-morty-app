package com.codescrubs.rickandmortyapp.ui.activities

import com.codescrubs.rickandmortyapp.data.api.DataSource
import com.codescrubs.rickandmortyapp.domain.Location
import com.codescrubs.rickandmortyapp.domain.PartialLocation
import com.codescrubs.rickandmortyapp.mvp.LocationDetailMVP
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class LocationDetailPresenter(private val view: LocationDetailMVP.View, private val partialLocation: PartialLocation) : LocationDetailMVP.Presenter, CoroutineScope {
    private val job = Job()
    private val dataSource by lazy { DataSource() }


    override val coroutineContext: CoroutineContext = job + Dispatchers.IO

    var location: Location? = null

    override fun onStart() {
        view.showProgress()

        launch {
            //  TODO: Refactor this into a standalone data provider class
            val result = dataSource.getLocation(partialLocation.url)
            location = result
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
        location?.let {
            view.showResidents(it)
        }
    }
}

