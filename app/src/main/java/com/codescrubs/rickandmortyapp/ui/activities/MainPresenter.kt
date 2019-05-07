package com.codescrubs.rickandmortyapp.ui.activities

import android.util.Log
import com.codescrubs.rickandmortyapp.data.api.RickAndMortyClient
import com.codescrubs.rickandmortyapp.domain.Character
import com.codescrubs.rickandmortyapp.mvp.MainMVP
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainPresenter(private val view: MainMVP.View) : MainMVP.Presenter, CoroutineScope {
    private val job = Job()

    override val coroutineContext: CoroutineContext = job + Dispatchers.IO

    override fun onStart() {
        refreshCharacters()
    }

    override fun onDestroy() {
        job.cancel()
    }

    override fun refresh() {
        refreshCharacters()
    }

    override fun characterClicked(character: Character) {
        Log.d("MainPresenter", character.name)
    }

    override fun onEndOfCharactersReached() {
        Log.d("MainPresenter", "Need to load more")
    }

    private fun refreshCharacters() {
        view.showProgress()

        launch {
            val result = RickAndMortyClient.rickAndMortyAPI.getCharactersAsync().await()

            withContext(Dispatchers.Main) {
                view.showCharacters(result.results)
                view.hideProgress()
            }
        }
    }
}

