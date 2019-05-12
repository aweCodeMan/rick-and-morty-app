package com.codescrubs.rickandmortyapp.mvp

import com.codescrubs.rickandmortyapp.domain.Location

interface LocationDetailMVP {

    interface View {
        fun showProgress()
        fun hideProgress()
        fun showLocation(location: Location)
        fun showResidents()
    }

    interface Presenter {
        fun onStart()
        fun onShowResidentsClicked()
        fun onDestroy()
    }
}