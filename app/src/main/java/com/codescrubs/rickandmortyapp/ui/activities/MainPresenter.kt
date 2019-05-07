package com.codescrubs.rickandmortyapp.ui.activities

import android.util.Log
import com.codescrubs.rickandmortyapp.data.api.RickAndMortyClient
import com.codescrubs.rickandmortyapp.data.api.response.PaginatedResult
import com.codescrubs.rickandmortyapp.domain.Character
import com.codescrubs.rickandmortyapp.mvp.MainMVP
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainPresenter(private val view: MainMVP.View) : MainMVP.Presenter, CoroutineScope {
    private val job = Job()

    private var paginatedCharacters: PaginatedResult<Character>? = null

    private var isLoadingNextPage = false

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

    override fun tryToLoadNextPage() {
        if (isLoadingNextPage) {
            return
        }

        loadNextPage()
    }

    private fun loadNextPage() {
        // Load a new page if we have an existing paginated result with a next link
        paginatedCharacters?.info!!.let {
            it.next?.let {
                isLoadingNextPage = true
                view.showProgress()

                launch {
                    val result = RickAndMortyClient.rickAndMortyAPI.getNextCharactersPageAsync(it).await()

                    paginatedCharacters = result

                    withContext(Dispatchers.Main) {
                        view.addCharacters(result.results)
                        view.hideProgress()
                        isLoadingNextPage = false
                    }
                }
            }
        }
    }

    private fun refreshCharacters() {
        view.showProgress()

        launch {
            val result = RickAndMortyClient.rickAndMortyAPI.getCharactersAsync().await()
            paginatedCharacters = result

            withContext(Dispatchers.Main) {
                view.showCharacters(result.results)
                view.hideProgress()
            }
        }
    }
}

