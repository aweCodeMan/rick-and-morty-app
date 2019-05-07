package com.codescrubs.rickandmortyapp.mvp

import com.codescrubs.rickandmortyapp.domain.Character

interface MainMVP {

    interface View {
        fun showProgress()
        fun hideProgress()
        fun showCharacters(characters: List<Character>)
        fun addCharacters(characters: List<Character>)
    }

    interface Presenter {
        fun onStart()
        fun onDestroy()
        fun characterClicked(character : Character)
        fun tryToLoadNextPage()
        fun refresh()
    }
}