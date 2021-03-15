package com.example.androidhomeapplication.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.androidhomeapplication.data.constans.Constants.DEFAULT_FIRST_PAGE

abstract class BasePagingSource<Item : Any> : PagingSource<Int, Item>() {

    open val firstPage: Int = DEFAULT_FIRST_PAGE

    override fun getRefreshKey(state: PagingState<Int, Item>): Int? = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> =
        try {
            val key = params.key ?: firstPage
            LoadResult.Page(
                data = loadData(params.key).orEmpty(),
                prevKey = if (key == firstPage) {
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
        pageKey: Int?
    ): List<Item>?
}