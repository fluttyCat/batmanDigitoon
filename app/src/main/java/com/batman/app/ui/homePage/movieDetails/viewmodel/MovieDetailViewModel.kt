package com.batman.app.ui.homePage.movieDetails.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import com.core.base.BaseViewModel
import com.core.dto.batman.BatmanDetailDto


abstract class MovieDetailViewModel(application: Application) : BaseViewModel(application) {

    abstract fun getBatmanDetailResponse(): LiveData<BatmanDetailDto>
    abstract fun requestBatmanDetail(apiKey: String, i: String)

}
