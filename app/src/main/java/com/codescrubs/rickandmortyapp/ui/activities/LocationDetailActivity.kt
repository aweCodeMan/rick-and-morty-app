package com.codescrubs.rickandmortyapp.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.codescrubs.rickandmortyapp.R
import com.codescrubs.rickandmortyapp.domain.Location
import com.codescrubs.rickandmortyapp.extensions.toast
import com.codescrubs.rickandmortyapp.mvp.LocationDetailMVP
import kotlinx.android.synthetic.main.activity_location_detail.*

class LocationDetailActivity : AppCompatActivity(), LocationDetailMVP.View {
    lateinit var presenter: LocationDetailMVP.Presenter

    companion object {
        const val LOCATION = "location"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_detail)

        presenter = LocationDetailPresenter(this, intent.getParcelableExtra(LOCATION))
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun showProgress() {
        swipeContainer.isRefreshing = true
    }

    override fun hideProgress() {
        swipeContainer.isRefreshing = false
    }

    override fun showLocation(location: Location) {
        name.text = location.name
        type.text = location.type
        dimension.text = getString(R.string.location_dimension, location.dimension)
        residents.text = getString(R.string.location_residents, location.residents.size.toString())

        cardResidents.setOnClickListener { presenter.onShowResidentsClicked()}
    }

    override fun showResidents() {
        toast("Show residents")
    }
}
