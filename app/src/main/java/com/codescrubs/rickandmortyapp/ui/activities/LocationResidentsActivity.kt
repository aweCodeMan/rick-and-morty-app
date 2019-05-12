package com.codescrubs.rickandmortyapp.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.codescrubs.rickandmortyapp.R
import com.codescrubs.rickandmortyapp.domain.Character
import com.codescrubs.rickandmortyapp.mvp.LocationResidentsMVP
import com.codescrubs.rickandmortyapp.ui.adapters.CharacterListAdapter
import kotlinx.android.synthetic.main.activity_location_residents.*
import kotlinx.android.synthetic.main.activity_location_residents.characterList
import org.jetbrains.anko.startActivity

class LocationResidentsActivity : AppCompatActivity(), LocationResidentsMVP.View {
    lateinit var presenter: LocationResidentsMVP.Presenter

    companion object {
        const val LOCATION = "location"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_residents)

        presenter = LocationResidentsPresenter(this, intent.getParcelableExtra(LOCATION))

        setupCharacterListRecyclerView()
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
        swipeCharacterListContainer.isEnabled = true
        swipeCharacterListContainer.isRefreshing = true
    }

    override fun hideProgress() {
        swipeCharacterListContainer.isRefreshing = false
        swipeCharacterListContainer.isEnabled = false
    }

    override fun showResidents(residents: List<Character>) {
        characterList.adapter = CharacterListAdapter(residents.toMutableList()) { presenter.onResidentClicked(it) }
    }

    private fun setupCharacterListRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        characterList.layoutManager = layoutManager
    }

    override fun showCharacterDetail(character: Character) {
        startActivity<CharacterDetailActivity>(CharacterDetailActivity.CHARACTER to character)
    }
}
