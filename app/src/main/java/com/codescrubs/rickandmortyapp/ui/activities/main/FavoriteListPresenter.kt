package com.codescrubs.rickandmortyapp.ui.activities.main

import com.codescrubs.rickandmortyapp.data.api.DataSource
import com.codescrubs.rickandmortyapp.domain.Character
import com.codescrubs.rickandmortyapp.mvp.FavoriteListMVP
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class FavoriteListPresenter(private val view: FavoriteListMVP.View) : FavoriteListMVP.Presenter, CoroutineScope {
    private val job = Job()

    private val dataSource by lazy { DataSource() }

    override val coroutineContext: CoroutineContext = job + Dispatchers.IO

    override fun onStart() {
        view.showProgress()

        launch {
            val result = dataSource.getFavoriteCharacters()

            withContext(Dispatchers.Main) {
                view.showCharacters(result)
                view.hideProgress()
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

