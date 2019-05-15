package com.codescrubs.rickandmortyapp.ui.activities.main


import android.arch.lifecycle.Lifecycle
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.codescrubs.rickandmortyapp.R
import com.codescrubs.rickandmortyapp.domain.Character
import com.codescrubs.rickandmortyapp.mvp.FavoriteListMVP
import com.codescrubs.rickandmortyapp.ui.activities.CharacterDetailActivity
import com.codescrubs.rickandmortyapp.ui.activities.base.BaseActivity
import com.codescrubs.rickandmortyapp.ui.adapters.CharacterListAdapter
import kotlinx.android.synthetic.main.fragment_character_list.characterList
import kotlinx.android.synthetic.main.fragment_character_list.swipeCharacterListContainer
import kotlinx.android.synthetic.main.fragment_favorite_list.*
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

            characterList.visibility = View.VISIBLE
            emptyState.visibility = View.GONE
        }
    }

    override fun showCharacterDetail(character: Character) {
        activity!!.startActivity<CharacterDetailActivity>(CharacterDetailActivity.CHARACTER to character)
    }

    override fun updateCharacter(character: Character) {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            (characterList.adapter as CharacterListAdapter).updateCharacter(character)
        }
    }

    override fun showEmptyState() {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            characterList.visibility = View.GONE
            emptyState.visibility = View.VISIBLE
        }
    }

    override fun removeCharacter(character: Character) {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            (characterList.adapter as CharacterListAdapter).removeCharacter(character)
        }
    }

    private fun setupCharacterListRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        characterList.layoutManager = layoutManager
    }

    override fun showError(message: String?) {
        message?.let {
            (activity as BaseActivity).showRetryError(characterList, message, getText(R.string.retry).toString()) { presenter.onStart() }
        }
    }
}
