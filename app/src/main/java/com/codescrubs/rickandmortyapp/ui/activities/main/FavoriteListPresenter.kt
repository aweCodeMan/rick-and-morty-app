package com.codescrubs.rickandmortyapp.ui.activities.main

import android.os.CountDownTimer
import com.codescrubs.rickandmortyapp.data.api.DataSource
import com.codescrubs.rickandmortyapp.domain.Character
import com.codescrubs.rickandmortyapp.mvp.FavoriteListMVP
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class FavoriteListPresenter(private val view: FavoriteListMVP.View) : FavoriteListMVP.Presenter, CoroutineScope {
    private val job = Job()
    private val dataSource by lazy { DataSource() }

    private val timers: MutableMap<Long, CountDownTimer> = mutableMapOf()

    override val coroutineContext: CoroutineContext = job + Dispatchers.IO


    companion object {
        private val COUNTDOWN_MILIS = 2000L
    }

    override fun onStart() {
        view.showProgress()

        launch {
            val result = dataSource.getFavoriteCharacters()

            withContext(Dispatchers.Main) {
                view.hideProgress()

                when (result.size) {
                    0 -> view.showEmptyState()
                    else -> view.showCharacters(result)
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
        timers[character.id]?.cancel()
        timers.remove(character.id)
    }

    override fun onCharacterUnfavored(character: Character) {
        view.updateCharacter(dataSource.unfavorCharacter(character))

        removeCharacterFromList(character)
    }

    private fun removeCharacterFromList(character: Character) {
        val countDownTimer = object : CountDownTimer(COUNTDOWN_MILIS, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                view.removeCharacter(character)
                timers.remove(character.id)
                checkEmptyState()
            }
        }

        timers[character.id] = countDownTimer
        countDownTimer.start()
    }

    private fun checkEmptyState() {
        if (dataSource.numberOfFavorites() == 0) {
            view.showEmptyState()
        }
    }
}

