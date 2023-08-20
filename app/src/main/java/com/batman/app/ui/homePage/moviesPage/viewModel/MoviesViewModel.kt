package com.batman.app.ui.homePage.moviesPage.viewModel

import android.app.Application
import androidx.paging.PagingData
import com.core.base.BaseViewModel
import com.core.dto.batman.BatmanDto
import kotlinx.coroutines.flow.Flow


abstract class MoviesViewModel(application: Application) : BaseViewModel(application) {

    abstract fun getMovieListData(): Flow<PagingData<BatmanDto>>

}