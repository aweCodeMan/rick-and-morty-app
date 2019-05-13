package com.codescrubs.rickandmortyapp.ui.activities.main


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.codescrubs.rickandmortyapp.R
import com.codescrubs.rickandmortyapp.domain.Character
import com.codescrubs.rickandmortyapp.mvp.CharacterListMVP
import com.codescrubs.rickandmortyapp.mvp.FavoriteListMVP
import com.codescrubs.rickandmortyapp.ui.activities.CharacterDetailActivity
import com.codescrubs.rickandmortyapp.ui.adapters.CharacterListAdapter
import kotlinx.android.synthetic.main.fragment_character_list.*
import org.jetbrains.anko.startActivity

class FavoriteListFragment : Fragment(), FavoriteListMVP.View {


    lateinit var presenter: FavoriteListMVP.Presenter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorite_list, container, false)
    }

    override fun onStart() {
        super.onStart()
        presenter = FavoriteListPresenter(this)

        setupCharacterListRecyclerView()

        presenter.onStart()
    }

    override fun showProgress() {
        swipeCharacterListContainer.isEnabled = true
        swipeCharacterListContainer.isRefreshing = true
    }

    override fun hideProgress() {
        swipeCharacterListContainer.isRefreshing = false
        swipeCharacterListContainer.isEnabled = false
    }

    override fun showCharacters(characters: List<Character>) {
        characterList.adapter = CharacterListAdapter(characters.toMutableList(), object : CharacterListAdapter.ItemListener{
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

    override fun showCharacterDetail(character: Character) {
        activity!!.startActivity<CharacterDetailActivity>(CharacterDetailActivity.CHARACTER to character)
    }

    private fun setupCharacterListRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        characterList.layoutManager = layoutManager
    }

    override fun updateCharacter(character: Character) {
        (characterList.adapter as CharacterListAdapter).updateCharacter(character)
    }
}
