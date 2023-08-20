package com.batman.app.ui.homePage.moviesPage.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.core.dto.NetworkState
import com.core.dto.batman.BatmanDto
import com.core.repository.HomeRepository
import com.core.repository.LocalRepository
import kotlinx.coroutines.flow.Flow


class MoviesViewModelImpl(
    application: Application,
    private var localRepository: LocalRepository,
    private var homeRepository: HomeRepository
) : MoviesViewModel(
    application
) {


    override fun getNetworkStatus(): LiveData<NetworkState> =
        MediatorLiveData<NetworkState>().apply {

        }

    override fun getMovieListData(): Flow<PagingData<BatmanDto>> {
        return homeRepository.getMoviesList().cachedIn(viewModelScope)
    }


}