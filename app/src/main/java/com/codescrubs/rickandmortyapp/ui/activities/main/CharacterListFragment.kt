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
import com.codescrubs.rickandmortyapp.ui.activities.CharacterDetailActivity
import com.codescrubs.rickandmortyapp.ui.adapters.CharacterListAdapter
import kotlinx.android.synthetic.main.fragment_character_list.*
import org.jetbrains.anko.startActivity

class CharacterListFragment : Fragment(), CharacterListMVP.View {
    lateinit var presenter: CharacterListMVP.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_character_list, container, false)
    }

    override fun onStart() {
        super.onStart()
        presenter = CharacterListPresenter(this)

        swipeCharacterListContainer.setOnRefreshListener { presenter.onRefreshSwiped() }

        setupCharacterListRecyclerView()

        presenter.onStart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroy()
    }

    override fun showProgress() {
        swipeCharacterListContainer.isRefreshing = true
    }

    override fun hideProgress() {
        swipeCharacterListContainer.isRefreshing = false
    }

    override fun showCharacters(characters: List<Character>) {
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

    override fun addCharacters(characters: List<Character>) {
        (characterList.adapter as CharacterListAdapter).addCharacters(characters)
    }

    override fun updateCharacter(character: Character) {
        (characterList.adapter as CharacterListAdapter).updateCharacter(character)
    }

    private fun setupCharacterListRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        characterList.layoutManager = layoutManager
        characterList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // Change this value to set how "far" from the end we try loading new characters
                val positionFromBottomOffset = 5

                if (layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.itemCount - positionFromBottomOffset) {
                    presenter.onEndOfListReached()
                }
            }
        })
    }

    override fun showCharacterDetail(character: Character) {
        activity!!.startActivity<CharacterDetailActivity>(CharacterDetailActivity.CHARACTER to character)
    }
}
