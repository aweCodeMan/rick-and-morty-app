package com.codescrubs.rickandmortyapp.ui.activities.main

import com.codescrubs.rickandmortyapp.data.api.DataSource
import com.codescrubs.rickandmortyapp.data.api.response.Result
import com.codescrubs.rickandmortyapp.data.api.response.PaginatedResult
import com.codescrubs.rickandmortyapp.domain.Character
import com.codescrubs.rickandmortyapp.mvp.CharacterListMVP
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class CharacterListPresenter(private val view: CharacterListMVP.View) : CharacterListMVP.Presenter, CoroutineScope {
    private val job = Job()
    private val dataSource by lazy { DataSource() }

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

        paginatedCharacters?.let {
            if (it.hasNextPage()) {
                loadNextPage(it.info.next)
            }
        }
    }

    private fun loadNextPage(nextUrl: String?) {
        nextUrl?.let {
            isLoadingNextPage = true
            view.showProgress()

            launch {
                val result = dataSource.getNextPageOfPaginatedCharacters(it)

                withContext(Dispatchers.Main) {
                    view.hideProgress()

                    when (result) {
                        is Result.Error -> view.showError(result.exception.message)
                        is Result.Success -> {
                            paginatedCharacters = result.data

                            view.addCharacters(result.data.results)
                            isLoadingNextPage = false
                        }
                    }
                }
            }
        }
    }

    private fun refreshCharacters() {
        view.showProgress()

        launch {
            val result = dataSource.getPaginatedCharacters()

            withContext(Dispatchers.Main) {
                view.hideProgress()

                when (result) {
                    is Result.Error -> view.showError(result.exception.message)
                    is Result.Success -> {
                        paginatedCharacters = result.data

                        view.showCharacters(result.data.results)
                    }
                }
            }
        }
    }

    override fun onCharacterFavored(character: Character) {
        view.updateCharacter(dataSource.favorCharacter(character))
    }

    override fun onCharacterUnfavored(character: Character) {
        view.updateCharacter(dataSource.unfavorCharacter(character))
    }
}

