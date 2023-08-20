package com.batman.app.ui.homePage.main.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.batman.app.ui.splashPage.splashActivity.viewModel.SplashViewModel
import com.core.dto.NetworkState
import com.core.repository.HomeRepository
import com.core.repository.LocalRepository


class MainViewModelImpl(
    application: Application,
    private var localRepository: LocalRepository,
    private var homeRepository: HomeRepository,
) : SplashViewModel(
    application
) {


    override fun getNetworkStatus(): LiveData<NetworkState> =
        MediatorLiveData<NetworkState>().apply {

        }


}