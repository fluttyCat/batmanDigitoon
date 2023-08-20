package com.core.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.core.api.HomeApi
import com.core.base.BaseRepository
import com.core.dataSource.BatmanListDataSource
import com.core.dto.batman.BatmanDto
import kotlinx.coroutines.flow.Flow

abstract class HomeRepository : BaseRepository {

    companion object {
        const val PAGE_SIZE = 10
    }

    abstract fun getMoviesList(): Flow<PagingData<BatmanDto>>
}

class HomeRepositoryImpl(private val homeApi: HomeApi) : HomeRepository() {

    override fun getMoviesList(): Flow<PagingData<BatmanDto>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = { BatmanListDataSource(homeApi) }
        ).flow
    }

}