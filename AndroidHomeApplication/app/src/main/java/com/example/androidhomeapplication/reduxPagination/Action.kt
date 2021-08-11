package com.example.androidhomeapplication.reduxPagination

sealed class Action {
    object Refresh : Action()
    object LoadMore : Action()
    object Restart : Action()
    data class NewPage<T>(val page: Int, val data: List<T>) : Action()
    data class Error(val throwable: Throwable) : Action()
}
