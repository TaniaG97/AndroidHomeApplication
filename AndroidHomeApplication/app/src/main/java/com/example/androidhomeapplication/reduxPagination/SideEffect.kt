package com.example.androidhomeapplication.reduxPagination

sealed class SideEffect {
    data class LoadPage(val page: Int) : SideEffect()
    data class ErrorEvent(val throwable: Throwable) : SideEffect()
}
