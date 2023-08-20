package com.batman.app.di

import android.app.Application
import androidx.room.Room
import com.core.dao.DigitoonDao
import com.core.db.DigitoonDb
import com.core.repository.LocalRepository
import com.core.repository.LocalRepositoryImpl
import com.core.utils.Preference
import com.core.utils.PreferenceImpl
import com.core.utils.SecurityHelper
import com.core.utils.SecurityHelperImpl
import com.core.utils.SettingManager
import com.core.utils.SettingManagerImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDb(app: Application): DigitoonDb {
        return Room
            .databaseBuilder(app, DigitoonDb::class.java, "cache.db")
            .fallbackToDestructiveMigration()
            .build()
    }


    @Singleton
    @Provides
    fun provideUserInfoDao(db: DigitoonDb): DigitoonDao {
        return db.userInfoDao()
    }

    @Provides
    fun provideSecurityHelper(): SecurityHelper {
        return SecurityHelperImpl()
    }

    @Provides
    fun providePreference(app: Application, securityHelper: SecurityHelper): Preference {
        return PreferenceImpl(app, securityHelper)
    }

    @Provides
    fun provideSettingManager(preference: Preference): SettingManager {
        return SettingManagerImpl(preference)
    }

    @Singleton
    @Provides
    fun provideLocalRepository(digitoonDao: DigitoonDao): LocalRepository {
        return LocalRepositoryImpl(digitoonDao)
    }


}
