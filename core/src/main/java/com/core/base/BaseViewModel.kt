package com.core.base


import android.app.Activity
import android.app.Application
import androidx.lifecycle.*
import com.core.dto.Coordinate
import com.core.dto.NetworkState
import com.core.utils.LocationHelper
import com.core.utils.LocationHelperImpl
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ir.co.rayanpay.common.dto.LocationState
import java.lang.ref.WeakReference

abstract class BaseViewModel(application: Application) : AndroidViewModel(application),
    LifecycleObserver {

    private val disposables = CompositeDisposable()

    private var locationHelper: LocationHelper = LocationHelperImpl()

    private val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    open fun getLocationState(): MutableLiveData<LocationState> = locationHelper.getLocationState()
    open fun onLocationUpdate(): MutableLiveData<Coordinate> = locationHelper.onLocationUpdate()
    open fun locationPermissionRequired(): MutableLiveData<ArrayList<String>> =
        locationHelper.locationPermissionRequired()

    open fun getNetworkStatus(): LiveData<NetworkState> = networkState

    open fun requestStartUpdatingLocation(
        activity: WeakReference<Activity>,
        requestEnable: Boolean? = true,
        fastInterval: Long? = 1 * 1000,
        interval: Long? = 1 * 1000
    ) {
        locationHelper.requestStartUpdatingLocation(activity, requestEnable, fastInterval, interval)
    }

    open fun requestStopUpdatingLocation() {
        locationHelper.requestStopUpdatingLocation()
    }

    open fun onPermissionGranted(activity: WeakReference<Activity>) {
        locationHelper.onPermissionGranted(activity)
    }

    open fun showingProgress() {
        networkState.postValue(NetworkState.loading())
    }

    open fun showingError(error: String) {
        networkState.postValue(NetworkState.error(msg = error))

    }

    open fun showingError(error: Int) {
        networkState.postValue(NetworkState.error(event = error))
    }

    open fun showingErrorCode(code: Int) {
        networkState.postValue(NetworkState.error(code = code))
    }


    open fun hideProgress() {
        networkState.postValue(NetworkState.loading())
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onCreated() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onDestroy() {
        locationHelper.requestStopUpdatingLocation()
    }

    open fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

    open fun disposeDisposables() {
        disposables.clear()
    }

    fun <T> addExecutorThreads(
        observable: Maybe<T>,
        onSuccess: ((T) -> Unit)? = null,
        onError: ((Throwable) -> Unit)? = null
    ) {
        addDisposable(
            observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribeOn(Schedulers.io()).subscribe({ result ->
                    onSuccess?.let {
                        onSuccess(result)
                    }
                }, { throwable ->
                    onError?.let {
                        onError(throwable)
                    }
                })
        )
    }

    fun <T> addExecutorThreads(
        observable: Single<T>,
        onSuccess: ((T) -> Unit)? = null,
        onError: ((Throwable) -> Unit)? = null
    ) {
        addDisposable(
            observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    onSuccess?.let {
                        onSuccess(result)
                    }
                }, { throwable ->
                    onError?.let {
                        onError(throwable)
                    }
                })
        )
    }

    fun <T> addExecutorThreads(
        observable: Observable<T>,
        onSuccess: ((T) -> Unit)? = null,
        onError: ((Throwable) -> Unit)? = null
    ) {
        addDisposable(
            observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe({ result ->
                    onSuccess?.let {
                        onSuccess(result)
                    }
                }, { throwable ->
                    onError?.let {
                        onError(throwable)
                    }
                })
        )
    }

    fun addExecutorThreads(
        observable: Completable,
        onSuccess: (() -> Unit)? = null,
        onError: ((Throwable) -> Unit)? = null
    ) {
        addDisposable(
            observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe({
                    onSuccess?.let {
                        onSuccess()
                    }
                }, { throwable ->
                    onError?.let {
                        onError(throwable)
                    }
                })
        )
    }
}