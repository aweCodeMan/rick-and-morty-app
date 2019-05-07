package com.codescrubs.rickandmortyapp.ui.activities

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toast
import com.codescrubs.rickandmortyapp.R
import com.codescrubs.rickandmortyapp.data.api.RickAndMortyClient
import com.codescrubs.rickandmortyapp.extensions.toast
import com.codescrubs.rickandmortyapp.ui.adapters.CharacterListAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_character_list -> {
                return@OnNavigationItemSelectedListener true
            }
           /* R.id.navigation_favorites -> {
                return@OnNavigationItemSelectedListener true
            }*/
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        characterList.layoutManager = LinearLayoutManager(this)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    override fun onStart() {
        super.onStart()

        launch {
            val result = RickAndMortyClient.rickAndMortyAPI.getCharactersAsync().await()
            val adapter = CharacterListAdapter(result.results) {
                toast(it.name)
            }

            runOnUiThread {
                characterList.adapter = adapter
            }
        }
    }
}
