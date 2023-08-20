package com.batman.app.util

import android.app.Application
import android.net.ConnectivityManager
import com.batman.app.util.NetworkUtil.isOnline
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


class ConnectivityInterceptor @Inject constructor(
    private val connectivityManager: ConnectivityManager,
    val app: Application
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {


        if (!isOnline(connectivityManager)) {
            throw NoConnectivityException()
        }

        val original = chain.request()

        val requestBuilder = original.newBuilder()
            /*.addHeader("Accept", "application/json")
            .addHeader("Content-type", "application/json")*/
            //.addHeader("apikey", "3e974fca")


        return chain.proceed(requestBuilder.build())
    }

}
