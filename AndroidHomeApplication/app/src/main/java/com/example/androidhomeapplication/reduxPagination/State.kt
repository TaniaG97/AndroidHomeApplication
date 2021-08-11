package com.example.androidhomeapplication.reduxPagination

sealed class State {
    object Empty : State()
    object EmptyProgress : State()
    data class Data<T>(val page: Int, val data: List<T>) : State()
    data class Refresh<T>(val page: Int, val data: List<T>) : State()
    data class NewPageProgress<T>(val page: Int, val data: List<T>) : State()
    data class FullData<T>(val page: Int, val data: List<T>) : State()
}
