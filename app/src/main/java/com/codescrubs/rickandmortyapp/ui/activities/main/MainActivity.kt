package com.codescrubs.rickandmortyapp.ui.activities.main

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.codescrubs.rickandmortyapp.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_character_list -> {
                showCharacterList()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_favorites -> {
                showFavorites()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun showFavorites() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.content, FavoriteListFragment(), FavoriteListFragment::javaClass.name)
            .addToBackStack(null)
            .commit()
    }

    private fun showCharacterList() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.content, CharacterListFragment(), CharacterListFragment::javaClass.name)
            .addToBackStack(null)
            .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.content, CharacterListFragment(), CharacterListFragment::javaClass.name)
                .addToBackStack(null)
                .commit()
        }
    }
}
