package com.codescrubs.rickandmortyapp.ui.activities

import android.os.Bundle
import com.codescrubs.rickandmortyapp.R
import com.codescrubs.rickandmortyapp.domain.Location
import com.codescrubs.rickandmortyapp.mvp.LocationDetailMVP
import com.codescrubs.rickandmortyapp.ui.activities.base.BaseActivity
import kotlinx.android.synthetic.main.activity_location_detail.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.startActivity

class LocationDetailActivity : BaseActivity(), LocationDetailMVP.View {
    lateinit var presenter: LocationDetailMVP.Presenter

    companion object {
        const val LOCATION = "location"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_detail)

        setSupportActionBar(toolbar)

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
        swipeContainer.isEnabled = true
        swipeContainer.isRefreshing = true
    }

    override fun hideProgress() {
        swipeContainer.isRefreshing = false
        swipeContainer.isEnabled = false
    }

    override fun showLocation(location: Location) {
        name.text = location.name
        type.text = location.type
        dimension.text = getString(R.string.location_dimension, location.dimension)
        residents.text = getString(R.string.location_residents, location.residents.size.toString())

        cardResidents.setOnClickListener { presenter.onShowResidentsClicked()}

        supportActionBar?.let {
            it.title = location.name
            it.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun showResidents(location: Location) {
        startActivity<LocationResidentsActivity>(LocationResidentsActivity.LOCATION to location)
    }
}
