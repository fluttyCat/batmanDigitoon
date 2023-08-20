package com.core.base

import androidx.lifecycle.LiveData
import com.core.dto.NetworkState
import com.core.utils.SingleLiveEvent

data class Response<T>(val onSuccess: SingleLiveEvent<T>, val networkState: LiveData<NetworkState>)