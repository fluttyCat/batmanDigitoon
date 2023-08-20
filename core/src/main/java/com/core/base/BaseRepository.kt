package com.core.base


import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.core.R
import com.core.dto.ErrorResultDto
import com.core.dto.NetworkState
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.io.EOFException
import java.io.IOException
import java.util.concurrent.Executors

interface BaseRepository {

    data class Response<T>(val onSuccess: LiveData<T>, val networkState: LiveData<NetworkState>)


    companion object {

        private val disposables = CompositeDisposable()

        val networkStatus: MutableLiveData<NetworkState> = MutableLiveData()

        private val ioThreads =
            Executors.newFixedThreadPool((Runtime.getRuntime().availableProcessors() * 2))

        val webRequestDefaultValue: Boolean = false


    }


    open fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

    open fun disposeDisposables() {
        //disposables.clear()
    }


    open fun showProgressAction(tag: String? = null): NetworkState {
        val network = NetworkState.loading(tag)
        networkStatus.postValue(network)
        return network
    }


    open fun hideProgressAction(tag: String? = null): NetworkState {
        val network = NetworkState.loaded(tag)
        networkStatus.postValue(network)
        return network
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


    /* fun <T, E> handlerResult(tag: String, result: ApiResultDto<T, E>): com.core.base.Response<T?> {
         if (result.success) {
             val data = result.data
             return if (data != null) {
                 hideProgressAction(tag)
                 Response(result.data, NetworkState.loaded(tag))
             } else {
                 Response(
                     result.data,
                     NetworkState.error(
                         R.string.NullPointException,
                         tag = tag,
                         result.message ?: ""
                     )
                 )
             }
         } else {
             return Response(
                 result.data, NetworkState.error(
                     R.string.Undefine,
                     tag = tag,
                     msg = result.message ?: ""
                 )
             )
         }
     }
 */

    fun handleError(tag: String? = null, t: Throwable?): NetworkState {

        val network: NetworkState = when (t) {
            is EOFException -> NetworkState.error(R.string.eofException, tag = tag)
            is IOException -> NetworkState.error(R.string.ioException, tag = tag)
            is SQLiteConstraintException -> NetworkState.error(
                R.string.sqLiteException,
                tag = tag,
                msg = t.message
            )
            is HttpException -> when (t.code()) {

                400 -> NetworkState.error(R.string.bad_request, tag = tag)

                401 -> NetworkState.error(
                    R.string.authorization,
                    tag = tag,
                    code = t.code(),
                    errorBody = Gson().fromJson(
                        t.response()?.errorBody()?.string(),
                        ErrorResultDto::class.java
                    )
                )
                402 -> NetworkState.error(R.string.payment, tag = tag)

                403 -> NetworkState.error(R.string.forbidden, tag = tag)

                404 -> NetworkState.error(R.string.not_found, tag = tag)

                405 -> NetworkState.error(R.string.not_allowed, tag = tag)

                406 -> NetworkState.error(R.string.not_acceptable, tag = tag)

                407 -> NetworkState.error(R.string.proxy, tag = tag)

                408 -> NetworkState.error(R.string.timeout, tag = tag)

                422 -> NetworkState.error(R.string.Error422, tag = tag)

                414 -> NetworkState.error(R.string.Error414, tag = tag)

                500 -> NetworkState.error(R.string.Error500, tag = tag)

                else -> NetworkState.error(R.string.runtimeException, tag = tag)
            }
            is JsonSyntaxException -> NetworkState.error(R.string.jsonFormat, tag = tag)


            else -> NetworkState.error(R.string.undefine, tag = tag)
        }
        networkStatus.postValue(network)
        return network
    }


}