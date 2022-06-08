package com.sproject.winlink.presentation.extensions

import android.view.View

fun View.visibleIf(visible: Boolean, goneWhenInvisible: Boolean = true) {
    visibility = if (visible) View.VISIBLE
    else if (goneWhenInvisible) View.GONE
    else View.INVISIBLE
}