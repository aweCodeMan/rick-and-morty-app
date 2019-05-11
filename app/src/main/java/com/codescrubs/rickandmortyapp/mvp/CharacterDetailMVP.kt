package com.codescrubs.rickandmortyapp.mvp

import com.codescrubs.rickandmortyapp.domain.Character
import com.codescrubs.rickandmortyapp.domain.Location

interface CharacterDetailMVP {

    interface View {
        fun showCharacter(character: Character)
        fun showLocationDetail(location: Location)
    }

    interface Presenter {
        fun onStart()
        fun onLocationClicked(location: Location)
    }
}