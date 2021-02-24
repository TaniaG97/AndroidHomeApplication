package com.example.androidhomeapplication


sealed class DataResult<out T : Any?> {
    class Default : DataResult<Nothing>()
    class Loading : DataResult<Nothing>()
    class EmptyResult : DataResult<Nothing>()
    class Success<out T : Any>(val value: T) : DataResult<T>()
    class Error(val error: Throwable) : DataResult<Nothing>()
}
