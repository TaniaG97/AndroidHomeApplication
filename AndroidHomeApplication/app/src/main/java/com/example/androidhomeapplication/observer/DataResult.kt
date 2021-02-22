package com.example.androidhomeapplication.observer


class DataResult<T> {

    companion object {
        fun <T> inProgress(): DataResult<T> {
            return DataResult(State.IN_PROGRESS)
        }
    }

    var data: T? = null
    var state: State? = null

    enum class State {
        IN_PROGRESS,
        SUCCESSFUL,
        ERROR
    }

    constructor(data: T?) {
        this.state = State.SUCCESSFUL
        this.data = data
    }

    constructor(state: State) {
        this.state = state
    }

    fun isSuccessful(): Boolean {
        return state == State.SUCCESSFUL
    }

}
