package com.codescrubs.rickandmortyapp.ui.activities

import com.codescrubs.rickandmortyapp.domain.Character
import com.codescrubs.rickandmortyapp.domain.PartialLocation
import com.codescrubs.rickandmortyapp.mvp.CharacterDetailMVP

class CharacterDetailPresenter(private val view: CharacterDetailMVP.View, private val character: Character) : CharacterDetailMVP.Presenter {

    override fun onStart() {
        view.showCharacter(character)
    }

    override fun onLocationClicked(partialLocation: PartialLocation) {
        view.showLocationDetail(partialLocation)
    }
}

