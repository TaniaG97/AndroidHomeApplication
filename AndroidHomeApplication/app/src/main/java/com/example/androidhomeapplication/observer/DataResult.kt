package com.example.androidhomeapplication.observer


class DataResult<T> {
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
}
