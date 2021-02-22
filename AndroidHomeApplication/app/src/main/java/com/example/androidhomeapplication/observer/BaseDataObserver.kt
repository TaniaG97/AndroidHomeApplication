package com.example.androidhomeapplication.observer

import androidx.lifecycle.Observer

open class BaseDataObserver<T> : Observer<DataResult<T>> {

    override fun onChanged(dataResult: DataResult<T>?) {
        if (dataResult != null) {
            when {
                dataResult.state == DataResult.State.IN_PROGRESS -> onInProgress()
                dataResult.state == DataResult.State.SUCCESSFUL -> {
                    if (dataResult.data != null) {
                        onData(dataResult.data!!)
                    } else {
                        onData()
                    }
                    onDone()
                }
                dataResult.state == DataResult.State.ERROR -> {
                    onError()
                    onDone()
                }
            }
        }
    }

    open fun onInProgress() {
    }

    open fun onData() {
    }

    open fun onData(data: T) {
    }

    open fun onError() {
    }

    open fun onDone() {
    }
}
