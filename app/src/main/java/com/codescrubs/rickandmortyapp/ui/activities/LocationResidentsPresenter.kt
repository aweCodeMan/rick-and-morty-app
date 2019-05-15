package com.codescrubs.rickandmortyapp.ui.activities

import com.codescrubs.rickandmortyapp.data.api.DataSource
import com.codescrubs.rickandmortyapp.data.api.response.Result
import com.codescrubs.rickandmortyapp.domain.Character
import com.codescrubs.rickandmortyapp.domain.Location
import com.codescrubs.rickandmortyapp.mvp.LocationResidentsMVP
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class LocationResidentsPresenter(private val view: LocationResidentsMVP.View, private val location: Location) :
    LocationResidentsMVP.Presenter, CoroutineScope {
    private val job = Job()
    private val dataSource by lazy { DataSource() }

    override val coroutineContext: CoroutineContext = job + Dispatchers.IO

    override fun onStart() {
        view.showProgress()

        launch {
            val result = dataSource.getCharactersByURLs(location.residents)

            withContext(Dispatchers.Main) {
                view.hideProgress()

                when (result) {
                    is Result.Error -> view.showError(result.exception.message)
                    is Result.Success -> {
                        view.showCharacters(result.data)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        job.cancel()
    }

    override fun onCharacterClicked(character: Character) {
        view.showCharacterDetail(character)
    }

    override fun onCharacterFavored(character: Character) {
        view.updateCharacter(dataSource.favorCharacter(character))
    }

    override fun onCharacterUnfavored(character: Character) {
        view.updateCharacter(dataSource.unfavorCharacter(character))
    }
}

