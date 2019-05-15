package com.codescrubs.rickandmortyapp.mvp.base

import com.codescrubs.rickandmortyapp.domain.Character

interface BaseCharacterListMVP {

    interface View {
        fun showCharacters(characters: List<Character>)
        fun showCharacterDetail(character: Character)
        fun updateCharacter(character: Character)
        fun showError(message: String?)
    }

    interface Presenter {
        fun onCharacterClicked(character: Character)
        fun onCharacterFavored(character: Character)
        fun onCharacterUnfavored(character: Character)
    }
}