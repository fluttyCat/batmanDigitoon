package com.core.dataSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.core.api.HomeApi
import com.core.dto.batman.BatmanDto

class BatmanListDataSource(
    private val homePageApi: HomeApi
) : PagingSource<Int, BatmanDto>() {

    override fun getRefreshKey(state: PagingState<Int, BatmanDto>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BatmanDto> {
        return try {
            val page = params.key ?: 1 // set page 1 as default
            val pageSize = params.loadSize
            val packagesResponse = homePageApi.getMoviesList("3e974fca","batman")
            val packagesItems = packagesResponse.search
            val prevKey = if (page > 1) page - 1 else null
            val nextKey = if (packagesItems?.isNotEmpty() == true) page + 1 else null
            LoadResult.Page(packagesItems!!, prevKey, nextKey)
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

}