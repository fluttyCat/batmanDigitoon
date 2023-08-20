package com.batman.app.ui.splashPage.splashFragment

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.WindowManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.batman.app.R
import com.batman.app.databinding.FragmentSplashBinding
import com.batman.app.di.DaggerAppComponent
import com.batman.app.ui.homePage.main.MainActivity
import com.batman.app.ui.splashPage.splashFragment.viewModel.SplashViewModel
import com.batman.app.ui.splashPage.splashFragment.viewModel.SplashViewModelImpl
import com.batman.app.util.RootUtil.isDeviceRooted
import com.core.base.ParentFragment
import com.core.repository.HomeRepository
import com.core.repository.LocalRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


class SplashFragment : ParentFragment<SplashViewModel, FragmentSplashBinding>() {

    @Inject
    lateinit var localRepository: LocalRepository

    @Inject
    lateinit var homeRepository: HomeRepository

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val isDebug = requireContext().applicationInfo.flags and
                ApplicationInfo.FLAG_DEBUGGABLE != 0

        if (!isDeviceRooted) {
            navigateToHomeActivity()
        }
        /*if (isDebug) {
            testDebugMode()
        }*/

        /*if(Settings.Secure.getInt(requireActivity().contentResolver, Settings.Secure.ADB_ENABLED, 0) == 1) {
            testDebugMode()
        }*/
    }

    private fun navigateToHomeActivity() {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(1000)
            val intentToHome =
                Intent(requireActivity(), MainActivity::class.java).apply {
                    startActivity(this)
                }

        }
    }

    private fun testDebugMode() {
        val view = layoutInflater.inflate(R.layout.dialog_debug_mood_layout, null)
        val debugModeAD = AlertDialog.Builder(requireContext())
            .setCancelable(false)
            .create()
        debugModeAD.setView(view)
        val window = debugModeAD.window
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        debugModeAD.show()
        Handler().postDelayed({
            requireActivity().finish()
            System.exit(0)
        },2000)


    }


    override fun getViewModelClass(): Class<SplashViewModel> = SplashViewModel::class.java

    override fun getFactory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SplashViewModelImpl(
                    application = requireActivity().application,
                    localRepository = localRepository,
                    homeRepository = homeRepository
                ) as T
            }
        }
    }

    override fun getResourceLayoutId(): Int = R.layout.fragment_splash

    override fun inject() {
        DaggerAppComponent.builder()
            .app(requireActivity().application)
            .build()
            .inject(this)
    }


}