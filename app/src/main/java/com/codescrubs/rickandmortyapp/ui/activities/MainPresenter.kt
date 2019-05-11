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
        if (paginatedCharacters == null) {
            refreshCharacters()
        }
    }

    override fun onDestroy() {
        job.cancel()
    }

    override fun onRefreshSwiped() {
        refreshCharacters()
    }

    override fun onCharacterClicked(character: Character) {
        view.showCharacterDetail(character)
    }

    override fun onEndOfListReached() {
        if (isLoadingNextPage) {
            return
        }

        loadNextPage()
    }

    //  TODO: Refactor this into a standalone data provider class
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

    //  TODO: Refactor this into a standalone data provider class
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

