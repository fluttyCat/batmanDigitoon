package com.batman.app.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.core.api.*
import com.core.repository.*
import dagger.Module
import dagger.Provides
import com.batman.app.util.ConnectivityInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideConnectivityManager(app: Application): ConnectivityManager {
        return app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }


    @Singleton
    @Provides
    fun provideConnectivityInterceptor(connectivityManager: ConnectivityManager,app: Application): ConnectivityInterceptor {
        return ConnectivityInterceptor(connectivityManager,app)
    }

    @Singleton
    @Provides
    fun provideOkHttp(connectivityInterceptor : ConnectivityInterceptor): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)


        httpClient.addInterceptor(connectivityInterceptor)

        return httpClient.build()
    }

    @Provides
    @Singleton
    @Named("retrofit_login")
    fun provideRetrofitLogin(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://www.omdbapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    fun provideLoginApi(@Named("retrofit_login") retrofit: Retrofit): HomeApi {
        return retrofit.create(HomeApi::class.java)
    }

    @Singleton
    @Provides
    fun provideLoginRepository(homeApi: HomeApi): HomeRepository {
        return HomeRepositoryImpl(homeApi)
    }

}
