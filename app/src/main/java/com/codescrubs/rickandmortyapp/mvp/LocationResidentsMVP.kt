package com.codescrubs.rickandmortyapp.mvp

import com.codescrubs.rickandmortyapp.mvp.base.BaseCharacterListMVP

interface LocationResidentsMVP {

    interface View : BaseCharacterListMVP.View {
        fun showProgress()
        fun hideProgress()
    }

    interface Presenter : BaseCharacterListMVP.Presenter {
        fun onStart()
        fun onDestroy()
    }
}