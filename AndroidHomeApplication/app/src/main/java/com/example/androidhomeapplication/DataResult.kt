package com.example.androidhomeapplication


sealed class DataResult<out T : Any?> {
    class Loading : DataResult<Nothing>()
    class EmptyResult : DataResult<Nothing>()
    class Success<T>(val value: T) : DataResult<T>()
    class Error(val error: Throwable) : DataResult<Nothing>()
}
