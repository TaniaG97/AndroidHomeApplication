package com.example.androidhomeapplication.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.androidhomeapplication.data.constans.Constants.DEFAULT_FIRST_PAGE

abstract class BasePagingSource<Item : Any> : PagingSource<Int, Item>() {

    open val firstPageKey: Int get() = DEFAULT_FIRST_PAGE

    override fun getRefreshKey(state: PagingState<Int, Item>): Int? = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> =
        try {
            val key = params.key ?: firstPageKey
            LoadResult.Page(
                data = loadData(key),
                prevKey = if (key == firstPageKey) {
                    null
                } else {
                    key - 1
                },
                nextKey = key + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    abstract suspend fun loadData(
        pageKey: Int
    ): List<Item>
}