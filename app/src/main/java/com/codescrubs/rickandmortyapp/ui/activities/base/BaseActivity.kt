package com.codescrubs.rickandmortyapp.ui.activities.base

import android.arch.lifecycle.Lifecycle
import android.graphics.Color
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.codescrubs.rickandmortyapp.R

abstract class BaseActivity : AppCompatActivity() {
    fun showRetryError(view: View, errorMessage: String, action: String, actionClickListener: (View) -> Unit) {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            val snackBar = Snackbar.make(view, errorMessage, Snackbar.LENGTH_INDEFINITE)
            snackBar.setAction(action) { actionClickListener(snackBar.view) }
            snackBar.setActionTextColor(Color.WHITE)
            snackBar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
            snackBar.show()
        }
    }
}