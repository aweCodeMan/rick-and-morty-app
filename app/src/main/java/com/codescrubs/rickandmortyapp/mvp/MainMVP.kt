package com.codescrubs.rickandmortyapp.mvp

import com.codescrubs.rickandmortyapp.domain.Character

interface MainMVP {

    interface View {
        fun showProgress()
        fun hideProgress()
        fun showCharacters(characters: List<Character>)
        fun addCharacters(characters: List<Character>)
        fun showCharacterDetail(character: Character)
    }

    interface Presenter {
        fun onStart()
        fun onDestroy()
        fun onCharacterClicked(character : Character)
        fun onEndOfListReached()
        fun onRefreshSwiped()
    }
}