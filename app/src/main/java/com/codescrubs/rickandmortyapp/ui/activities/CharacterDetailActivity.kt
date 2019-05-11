package com.codescrubs.rickandmortyapp.ui.activities

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.codescrubs.rickandmortyapp.R
import com.codescrubs.rickandmortyapp.domain.Character
import com.codescrubs.rickandmortyapp.domain.Location
import com.codescrubs.rickandmortyapp.extensions.toast
import com.codescrubs.rickandmortyapp.mvp.CharacterDetailMVP
import kotlinx.android.synthetic.main.activity_character_detail.*


class CharacterDetailActivity : AppCompatActivity(), CharacterDetailMVP.View {
    lateinit var presenter: CharacterDetailMVP.Presenter

    companion object {
        const val CHARACTER = "character"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_detail)
        presenter = CharacterDetailPresenter(this, intent.getParcelableExtra(CHARACTER))
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun showCharacter(character: Character) {
        name.text = character.name
        status.text = character.status
        species.text = character.species
        gender.text = character.gender
        episodes.text = resources.getQuantityString(R.plurals.character_episodes, character.episode.size, character.episode.size.toString())

        origin.text = character.origin.name
        location.text = character.location.name

        Glide.with(this).load(character.image).centerCrop().into(image)
        image.contentDescription = character.name

        cardLocation.setOnClickListener { presenter.onLocationClicked(character.location) }
        cardOrigin.setOnClickListener { presenter.onLocationClicked(character.origin) }
    }

    override fun showLocationDetail(location: Location) {
        toast(location.name)
    }
}
