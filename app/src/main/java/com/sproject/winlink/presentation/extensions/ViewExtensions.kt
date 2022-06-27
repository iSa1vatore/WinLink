package com.sproject.winlink.presentation.extensions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun View.visibleIf(visible: Boolean, goneWhenInvisible: Boolean = true) {
    visibility = if (visible) View.VISIBLE
    else if (goneWhenInvisible) View.GONE
    else View.INVISIBLE
}

fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}
