package com.codescrubs.rickandmortyapp

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.codescrubs.rickandmortyapp.ui.activities.main.MainActivity
import com.codescrubs.rickandmortyapp.ui.adapters.CharacterListAdapter

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Rule
import com.codescrubs.rickandmortyapp.rules.OkHttpIdlingResourceRule

@RunWith(AndroidJUnit4::class)
class CharacterListInstrumentedTest {

    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @get:Rule
    var rule = OkHttpIdlingResourceRule()

    @Test
    fun opensCharacterDetailWhenClickedOnListItem() {
        //  I know, I know :\
        Thread.sleep(1000)
        onView(withId(R.id.characterList))
            .perform(RecyclerViewActions.actionOnItemAtPosition<CharacterListAdapter.ViewHolder>(0, click()))

        onView(withId(R.id.name)).check(matches(withText("Rick Sanchez")))
    }
}

