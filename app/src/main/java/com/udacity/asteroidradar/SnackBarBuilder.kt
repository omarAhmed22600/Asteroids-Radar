package com.udacity.asteroidradar

import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

fun SnackBarBuilder(snackbarText: String, view: View) {
    val snackbar = Snackbar.make(view, snackbarText, Snackbar.LENGTH_SHORT)
    val backgroundColor = ContextCompat.getColor(view.context, R.color.colorAccent)
    snackbar.view.setBackgroundColor(backgroundColor)
    snackbar.show()
}