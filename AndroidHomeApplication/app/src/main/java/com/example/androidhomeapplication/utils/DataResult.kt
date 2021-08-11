package com.example.androidhomeapplication.utils


sealed class DataResult<out T : Any?> {
    class Success<out T : Any>(val value: T) : DataResult<T>()
    class Error(val error: Throwable) : DataResult<Nothing>()
}
