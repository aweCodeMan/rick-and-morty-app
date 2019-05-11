package com.codescrubs.rickandmortyapp.ui.activities

import android.util.Log
import com.codescrubs.rickandmortyapp.data.api.RickAndMortyClient
import com.codescrubs.rickandmortyapp.data.api.response.PaginatedResult
import com.codescrubs.rickandmortyapp.domain.Character
import com.codescrubs.rickandmortyapp.domain.Location
import com.codescrubs.rickandmortyapp.mvp.CharacterDetailMVP
import com.codescrubs.rickandmortyapp.mvp.MainMVP
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class CharacterDetailPresenter(private val view: CharacterDetailMVP.View, private val character: Character) : CharacterDetailMVP.Presenter {

    override fun onStart() {
        view.showCharacter(character)
    }

    override fun onLocationClicked(location: Location) {
        view.showLocationDetail(location)
    }
}

