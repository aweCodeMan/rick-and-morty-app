package com.codescrubs.rickandmortyapp.ui.activities

import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.bumptech.glide.Glide
import com.codescrubs.rickandmortyapp.R
import com.codescrubs.rickandmortyapp.domain.Character
import com.codescrubs.rickandmortyapp.domain.PartialLocation
import com.codescrubs.rickandmortyapp.mvp.CharacterDetailMVP
import com.codescrubs.rickandmortyapp.ui.activities.base.BaseActivity
import kotlinx.android.synthetic.main.activity_character_detail.*
import kotlinx.android.synthetic.main.activity_character_detail.favorite
import kotlinx.android.synthetic.main.activity_character_detail.image
import kotlinx.android.synthetic.main.activity_character_detail.location
import kotlinx.android.synthetic.main.activity_character_detail.name
import kotlinx.android.synthetic.main.activity_character_detail.origin
import kotlinx.android.synthetic.main.activity_character_detail.status
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.startActivity


class CharacterDetailActivity : BaseActivity(), CharacterDetailMVP.View {
    lateinit var presenter: CharacterDetailMVP.Presenter

    companion object {
        const val CHARACTER = "character"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_detail)

        setSupportActionBar(toolbar)

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

        when (character.isFavorite) {
            true -> favorite.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_favorite_24dp
                )
            )
            false -> favorite.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_favorite_border_24dp
                )
            )
        }

        favorite.setOnClickListener { if (character.isFavorite) presenter.onCharacterUnfavored(character) else presenter.onCharacterFavored(character)}

        supportActionBar?.let {
            it.title = character.name
            it.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun showLocationDetail(partialLocation: PartialLocation) {
        startActivity<LocationDetailActivity>(LocationDetailActivity.LOCATION to partialLocation)
    }
}
