package com.sproject.winlink.presentation.extensions

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.sproject.winlink.R

fun Fragment.findTopNavController(): NavController {
    val topLevelHost = requireActivity()
        .supportFragmentManager
        .findFragmentById(R.id.fragmentContainer) as NavHostFragment?

    return topLevelHost?.navController ?: findNavController()
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(
        requireContext(),
        message,
        duration
    ).show()
}

fun Fragment.navigate(route: NavDirections) {
    findNavController().navigate(route)
}

fun hideViews(vararg views: View) {
    views.forEach { it.visibleIf(false) }
}

fun showViews(vararg views: View) {
    views.forEach { it.visibleIf(true) }
}
