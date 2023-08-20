package com.batman.app.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides


@Module(includes = [NetworkModule::class, DatabaseModule::class])
class AppModule{
    @Provides
    fun provideSharedPreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences("encryption", Context.MODE_PRIVATE)
    }
}