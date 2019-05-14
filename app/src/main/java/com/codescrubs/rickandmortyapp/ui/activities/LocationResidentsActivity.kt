package com.codescrubs.rickandmortyapp.ui.activities

import android.arch.lifecycle.Lifecycle
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import com.codescrubs.rickandmortyapp.R
import com.codescrubs.rickandmortyapp.domain.Character
import com.codescrubs.rickandmortyapp.domain.Location
import com.codescrubs.rickandmortyapp.mvp.LocationResidentsMVP
import com.codescrubs.rickandmortyapp.ui.activities.base.BaseActivity
import com.codescrubs.rickandmortyapp.ui.adapters.CharacterListAdapter
import kotlinx.android.synthetic.main.activity_location_residents.*
import org.jetbrains.anko.startActivity

class LocationResidentsActivity : BaseActivity(), LocationResidentsMVP.View {
    lateinit var presenter: LocationResidentsMVP.Presenter

    companion object {
        const val LOCATION = "location"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_residents)

        val location: Location = intent.getParcelableExtra(LOCATION)
        presenter = LocationResidentsPresenter(this, location)

        setupCharacterListRecyclerView()

        setSupportActionBar(toolbar as Toolbar?)

        supportActionBar?.let {
            it.title = getString(R.string.location_residents_title, location.name)
            it.setDisplayHomeAsUpEnabled(true)
            (toolbar as Toolbar?)?.setNavigationOnClickListener { finish() }
        }
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
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            swipeCharacterListContainer.isEnabled = true
            swipeCharacterListContainer.isRefreshing = true
        }
    }

    override fun hideProgress() {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            swipeCharacterListContainer.isRefreshing = false
            swipeCharacterListContainer.isEnabled = false
        }
    }

    override fun showCharacters(characters: List<Character>) {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            characterList.adapter =
                CharacterListAdapter(characters.toMutableList(), object : CharacterListAdapter.ItemListener {
                    override fun onItemClick(character: Character) {
                        presenter.onCharacterClicked(character)
                    }

                    override fun onFavoriteClick(character: Character) {
                        presenter.onCharacterFavored(character)
                    }

                    override fun onUnfavoriteClick(character: Character) {
                        presenter.onCharacterUnfavored(character)
                    }
                })
        }
    }

    override fun showCharacterDetail(character: Character) {
        startActivity<CharacterDetailActivity>(CharacterDetailActivity.CHARACTER to character)
    }

    override fun updateCharacter(character: Character) {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            (characterList.adapter as CharacterListAdapter).updateCharacter(character)
        }
    }

    private fun setupCharacterListRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        characterList.layoutManager = layoutManager
    }
}
