package com.codescrubs.rickandmortyapp.mvp

import com.codescrubs.rickandmortyapp.domain.Character
import com.codescrubs.rickandmortyapp.domain.PartialLocation

interface LocationResidentsMVP {

    interface View {
        fun showProgress()
        fun hideProgress()
        fun showResidents(residents: List<Character>)
        fun showCharacterDetail(character: Character)
    }

    interface Presenter {
        fun onStart()
        fun onResidentClicked(character: Character)
        fun onDestroy()
    }
}