package com.codescrubs.rickandmortyapp.mvp

import com.codescrubs.rickandmortyapp.domain.Character
import com.codescrubs.rickandmortyapp.mvp.base.BaseCharacterListMVP

interface CharacterListMVP {

    interface View: BaseCharacterListMVP.View {
        fun showProgress()
        fun hideProgress()
        fun addCharacters(characters: List<Character>)
    }

    interface Presenter: BaseCharacterListMVP.Presenter {
        fun onStart()
        fun onDestroy()
        fun onRefreshSwiped()
        fun onEndOfListReached()
    }
}