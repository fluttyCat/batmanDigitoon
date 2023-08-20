package com.batman.app.ui.splashPage.splashActivity

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.batman.app.R
import com.batman.app.databinding.ActivityMainBinding
import com.batman.app.di.DaggerAppComponent
import com.batman.app.ui.splashPage.splashActivity.viewModel.SplashViewModel
import com.batman.app.ui.splashPage.splashActivity.viewModel.SplashViewModelImpl
import com.core.base.ParentActivity
import com.core.repository.HomeRepository
import com.core.repository.LocalRepository
import javax.inject.Inject

class SplashActivity : ParentActivity<SplashViewModel, ActivityMainBinding>() {

    @Inject
    lateinit var localRepository: LocalRepository

    @Inject
    lateinit var homeRepository: HomeRepository

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.splashNavHost) as NavHostFragment? ?: return

        navController = host.navController


    }


    override fun getFactory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SplashViewModelImpl(
                    application = application,
                    localRepository = localRepository,
                    homeRepository = homeRepository
                ) as T
            }
        }
    }

    override fun inject() {
        DaggerAppComponent.builder()
            .app(application)
            .build()
            .inject(this)
    }

    override fun getResourceLayoutId(): Int = R.layout.activity_splash

    override fun getViewModelClass(): Class<SplashViewModel> = SplashViewModel::class.java

    override fun showProgress(tag: String) {
        super.showProgress(tag)

    }

    override fun hideProgress(tag: String) {
        super.hideProgress(tag)
    }

    override fun showError(tag: String, error: String) {
        super.showError(tag, error)
        Toast.makeText(this@SplashActivity, error, Toast.LENGTH_SHORT).show()
    }

}
