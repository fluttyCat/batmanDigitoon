package com.core.repository

import android.content.ContentValues
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.core.api.HomeApi
import com.core.base.BaseObserver.Companion.networkStatus
import com.core.base.BaseRepository
import com.core.base.Response
import com.core.dataSource.BatmanListDataSource
import com.core.dto.batman.BatmanDetailDto
import com.core.dto.batman.BatmanDetailsRequestDto
import com.core.dto.batman.BatmanDto
import com.core.utils.SingleLiveEvent
import kotlinx.coroutines.flow.Flow

abstract class HomeRepository : BaseRepository {

    companion object {
        const val PAGE_SIZE = 10
    }

    abstract fun getMoviesList(): Flow<PagingData<BatmanDto>>
    abstract fun getMovieDetails(batmanDetailsRequestDto: BatmanDetailsRequestDto): Response<BatmanDetailDto>
}

class HomeRepositoryImpl(private val homeApi: HomeApi) : HomeRepository() {

    override fun getMoviesList(): Flow<PagingData<BatmanDto>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = { BatmanListDataSource(homeApi) }
        ).flow
    }

    override fun getMovieDetails(batmanDetailsRequestDto: BatmanDetailsRequestDto): Response<BatmanDetailDto> {
        val tag = "${this::class.java.simpleName}_getMovieDetails"
        val data = SingleLiveEvent<BatmanDetailDto>()
        showProgressAction(tag)
        addExecutorThreads(homeApi.getMovieDetails(
            batmanDetailsRequestDto.apikey,
            batmanDetailsRequestDto.i
        ), onSuccess = { result ->
            data.postValue(result)
            hideProgressAction(tag)
        }, onError = {
            hideProgressAction(ContentValues.TAG)
            handleError(ContentValues.TAG, it)
        })
        return Response(data, networkStatus)
    }
}