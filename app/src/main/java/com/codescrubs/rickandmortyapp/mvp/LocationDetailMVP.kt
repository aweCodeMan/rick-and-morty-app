package com.codescrubs.rickandmortyapp.mvp

import com.codescrubs.rickandmortyapp.domain.Location

interface LocationDetailMVP {

    interface View {
        fun showProgress()
        fun hideProgress()
        fun showLocation(location: Location)
        fun showResidents(location: Location)
        fun showError(message: String?)
    }

    interface Presenter {
        fun onStart()
        fun onShowResidentsClicked()
        fun onDestroy()
    }
}