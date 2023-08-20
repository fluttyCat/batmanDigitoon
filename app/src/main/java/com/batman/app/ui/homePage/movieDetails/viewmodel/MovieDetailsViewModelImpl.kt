package com.batman.app.ui.homePage.movieDetails.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.core.dto.NetworkState
import com.core.dto.batman.BatmanDetailDto
import com.core.dto.batman.BatmanDetailsRequestDto
import com.core.repository.HomeRepository
import com.core.repository.LocalRepository

class MovieDetailsViewModelImpl(
    application: Application,
    private var localRepository: LocalRepository,
    private var homeRepository: HomeRepository,

    ) : MovieDetailViewModel(application) {


    private val movieDetail: MutableLiveData<BatmanDetailsRequestDto> = MutableLiveData()

    private val movieDetailRepo = Transformations.map(movieDetail) {
        homeRepository.getMovieDetails(it)
    }

    override fun getNetworkStatus(): LiveData<NetworkState> =
        MediatorLiveData<NetworkState>().apply {
            this.addSource(Transformations.switchMap(movieDetailRepo) { it.networkState }) {
                this.postValue(it)
            }
        }


    override fun getBatmanDetailResponse(): LiveData<BatmanDetailDto> =
        Transformations.switchMap(movieDetailRepo) {
            it.onSuccess
        }

    override fun requestBatmanDetail(apiKey: String, i: String) {
        movieDetail.postValue(BatmanDetailsRequestDto(apikey = apiKey, i = i))
    }

}