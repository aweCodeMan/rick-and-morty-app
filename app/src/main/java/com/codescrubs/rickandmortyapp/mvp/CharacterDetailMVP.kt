package com.codescrubs.rickandmortyapp.mvp

import com.codescrubs.rickandmortyapp.domain.Character
import com.codescrubs.rickandmortyapp.domain.PartialLocation

interface CharacterDetailMVP {

    interface View {
        fun showCharacter(character: Character)
        fun showLocationDetail(partialLocation: PartialLocation)
    }

    interface Presenter {
        fun onStart()
        fun onLocationClicked(partialLocation: PartialLocation)
        fun onCharacterFavored(character: Character)
        fun onCharacterUnfavored(character: Character)
    }
}