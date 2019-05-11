package com.codescrubs.rickandmortyapp.ui.activities

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.codescrubs.rickandmortyapp.R
import com.codescrubs.rickandmortyapp.domain.Character
import com.codescrubs.rickandmortyapp.mvp.MainMVP
import com.codescrubs.rickandmortyapp.ui.adapters.CharacterListAdapter
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity(), MainMVP.View {
    lateinit var presenter: MainMVP.Presenter

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_character_list -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_favorites -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainPresenter(this)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        swipeCharacterListContainer.setOnRefreshListener { presenter.onRefreshSwiped() }

        setupCharacterListRecyclerView()
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun showProgress() {
        swipeCharacterListContainer.isRefreshing = true
    }

    override fun hideProgress() {
        swipeCharacterListContainer.isRefreshing = false
    }

    override fun showCharacters(characters: List<Character>) {
        characterList.adapter = CharacterListAdapter(characters.toMutableList()) { presenter.onCharacterClicked(it) }
    }

    override fun addCharacters(characters: List<Character>) {
        (characterList.adapter as CharacterListAdapter).addCharacters(characters)
    }

    private fun setupCharacterListRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
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
        startActivity<CharacterDetailActivity>(CharacterDetailActivity.CHARACTER to character)
    }
}
