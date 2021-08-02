package com.example.androidhomeapplication.reduxPagination

class Store<T> {
    private var state: State = State.Empty

    var render: (State) -> Unit = {}
    var sideEffects: (SideEffect) -> Unit = {}

    fun proceed(action: Action) {
        val newState = reducer<T>(action, state) { sideEffect ->
            sideEffects(sideEffect) // вынести на другой поток
        }
        if (newState != state) {
            state = newState
            render(state)
        }
    }

    private fun <T> reducer(
        action: Action,
        state: State,
        sideEffectListener: (SideEffect) -> Unit
    ): State = when (action) {
        is Action.Restart -> {
            sideEffectListener(SideEffect.LoadPage(1))
            State.EmptyProgress
        }
        is Action.Refresh -> {
            when (state) {
                is State.Empty -> {
                    sideEffectListener(SideEffect.LoadPage(1))
                    State.EmptyProgress
                }
                is State.Data<*> -> {
                    sideEffectListener(SideEffect.LoadPage(1))
                    State.Refresh(state.page, state.data as List<T>)
                }
                is State.NewPageProgress<*> -> {
                    sideEffectListener(SideEffect.LoadPage(1))
                    State.Refresh(state.page, state.data as List<T>)
                }
                is State.FullData<*> -> {
                    sideEffectListener(SideEffect.LoadPage(1))
                    State.Refresh(state.page, state.data as List<T>)
                }
                else -> state
            }
        }
        is Action.LoadMore -> {
            when (state) {
                is State.Data<*> -> {
                    sideEffectListener(SideEffect.LoadPage(state.page + 1))
                    State.NewPageProgress(state.page, state.data as List<T>)
                }
                else -> state
            }
        }
        is Action.NewPage<*> -> {
            val items = action.data as List<T>
            when (state) {
                is State.EmptyProgress -> {
                    if (items.isEmpty()) {
                        State.Empty
                    } else {
                        State.Data(1, items)
                    }
                }
                is State.Refresh<*> -> {
                    if (items.isEmpty()) {
                        State.Empty
                    } else {
                        State.Data(1, items)
                    }
                }
                is State.NewPageProgress<*> -> {
                    if (items.isEmpty()) {
                        State.FullData(state.page, state.data as List<T>)
                    } else {
                        State.Data(state.page + 1, state.data as List<T> + items)
                    }
                }
                else -> state
            }
        }
        is Action.Error -> {
            when (state) {
                is State.EmptyProgress -> {
                    sideEffectListener(SideEffect.ErrorEvent(action.throwable))
                    State.Empty
                }
                is State.Refresh<*> -> {
                    sideEffectListener(SideEffect.ErrorEvent(action.throwable))
                    State.Data(state.page, state.data as List<T>)
                }
                is State.NewPageProgress<*> -> {
                    sideEffectListener(SideEffect.ErrorEvent(action.throwable))
                    State.Data(state.page, state.data as List<T>)
                }
                else -> state
            }
        }
    }
}