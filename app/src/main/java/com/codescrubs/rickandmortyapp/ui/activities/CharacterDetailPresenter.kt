package com.codescrubs.rickandmortyapp.ui.activities

import com.codescrubs.rickandmortyapp.data.api.DataSource
import com.codescrubs.rickandmortyapp.domain.Character
import com.codescrubs.rickandmortyapp.domain.PartialLocation
import com.codescrubs.rickandmortyapp.mvp.CharacterDetailMVP

class CharacterDetailPresenter(private val view: CharacterDetailMVP.View, private val character: Character) : CharacterDetailMVP.Presenter {
    private val dataSource by lazy { DataSource() }

    override fun onStart() {
        view.showCharacter(character)
    }

    override fun onLocationClicked(partialLocation: PartialLocation) {
        if(partialLocation.name.toLowerCase() == "unknown") {
            view.showUnknownLocationNotification()
        } else {
            view.showLocationDetail(partialLocation)
        }
    }

    override fun onCharacterFavored(character: Character) {
        view.showCharacter(dataSource.favorCharacter(character))
    }

    override fun onCharacterUnfavored(character: Character) {
        view.showCharacter(dataSource.unfavorCharacter(character))
    }
}

