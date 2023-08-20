package com.core.base

import androidx.lifecycle.LiveData
import com.core.dto.NetworkState

data class ResponseAsync<T>(val onSuccess: T, val networkState: NetworkState)