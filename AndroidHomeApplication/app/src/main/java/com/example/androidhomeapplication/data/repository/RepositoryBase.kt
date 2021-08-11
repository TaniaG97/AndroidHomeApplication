package com.example.androidhomeapplication.data.repository

import android.util.MalformedJsonException
import com.example.androidhomeapplication.utils.DataResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException

open class RepositoryBase {

    suspend inline fun <T> safeApiCall(crossinline apiCall: suspend () -> Response<T>): DataResult<T>? {
        return try {
            withContext(Dispatchers.IO) {
                val response = apiCall.invoke()
                if (response.isSuccessful) {
                    DataResult.Success(response.body()!!)
                } else {
                    DataResult.Error(Throwable("code: ${response.code()} errorBody: ${response.errorBody()}"))
                }
            }
        } catch (throwable: Throwable) {
            when (throwable) {
                is HttpException -> {
                    DataResult.Error(Throwable("HttpException: code: ${throwable.code()} errorBody: ${throwable.response()?.errorBody()}"))}
                is SocketTimeoutException -> {
                    DataResult.Error(Throwable("SocketTimeoutException"))
                }
                is MalformedJsonException -> {
                    DataResult.Error(Throwable("MalformedJsonException"))
                }
                else -> {
                    DataResult.Error(Throwable("Unknown Error: ${throwable.message ?: throwable.javaClass.simpleName}"))
                }
            }
        }
    }
}
