package com.sproject.winlink.presentation.base

sealed class ViewModelEvent {

    data class ShowToast(val message: String) : ViewModelEvent()

    data class Navigate(val route: Int) : ViewModelEvent()

}