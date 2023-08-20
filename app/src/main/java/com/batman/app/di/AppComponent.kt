package com.batman.app.di

import android.app.Application
import com.batman.app.ui.homePage.movieDetails.MovieDetailsFragment
import com.batman.app.ui.homePage.main.MainActivity
import com.batman.app.ui.homePage.moviesPage.MoviesFragment
import com.batman.app.ui.splashPage.splashActivity.SplashActivity
import com.batman.app.ui.splashPage.splashFragment.SplashFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class]
)
interface AppComponent {

    fun inject(app: Application)

    //Activity
    fun inject(app: MainActivity)
    fun inject(app: SplashActivity)

    //Fragment
    fun inject(app: SplashFragment)
    fun inject(app: MoviesFragment)
    fun inject(app: MovieDetailsFragment)


    //bottom sheet

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun app(app: Application): Builder

        fun build(): AppComponent
    }


}
