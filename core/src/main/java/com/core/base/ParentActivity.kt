package com.core.base

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.core.dto.Coordinate
import com.core.dto.Status
import ir.co.rayanpay.common.dto.LocationState
import java.lang.ref.WeakReference

typealias PermissionGranted = (Array<String>) -> Unit
typealias PermissionDenied = (Array<String>) -> Unit

abstract class ParentActivity<T : BaseViewModel, E : ViewDataBinding> : AppCompatActivity() {

    private val permissionRequest: Int = 12000

    var granted: PermissionGranted? = null

    var denied: PermissionDenied? = null

    lateinit var viewModel: T

    lateinit var dataBinding: E

    abstract fun getResourceLayoutId(): Int

    abstract fun getViewModelClass(): Class<T>

    abstract fun getFactory(): ViewModelProvider.Factory

    open fun inject() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
        dataBinding = DataBindingUtil.setContentView(this, getResourceLayoutId())
        viewModel = ViewModelProvider(this, getFactory()).get(getViewModelClass())
        viewModel.getNetworkStatus().observe(this, Observer {
            when (it.status) {
                Status.RUNNING -> showProgress(it.tag ?: "")
                Status.SUCCESS -> hideProgress(it.tag ?: "")
                else -> showError(it.tag ?: "", it.msg ?: getString(it.event))
            }
        })
        viewModel.getLocationState().observe(this, Observer {
            locationState(it)
        })
        viewModel.onLocationUpdate().observe(this, Observer {
            locationUpdate(it)
        })
        viewModel.locationPermissionRequired().observe(this, Observer {
            permissionsRequest(it.toTypedArray(), {
                requestStartUpdatingLocation()
            }, {
                /**
                 * cannot update location
                 */
            })
        })
        lifecycle.addObserver(viewModel)
        viewModel.onCreated()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    protected fun showFragment(
        @IdRes layoutId: Int,
        fragment: Fragment,
        addToBackStack: Boolean = false
    ) {
        val transaction = supportFragmentManager.beginTransaction()
            .replace(layoutId, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(fragment::class.java.simpleName)
        }
        transaction.commit()
    }

    protected fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    protected fun showMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    open fun initToolbar(toolbar: Toolbar, title: String? = null) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = title ?: ""
    }

    protected fun requestStartUpdatingLocation(requestEnableSetting: Boolean? = true) {
        viewModel.requestStartUpdatingLocation(WeakReference(this), requestEnableSetting)
    }

    protected fun requestStopUpdatingLocation() {
        viewModel.requestStopUpdatingLocation()
    }

    protected fun onLocationPermissionGranted() {
        viewModel.onPermissionGranted(WeakReference(this))
    }

    open fun locationState(state: LocationState) {}
    open fun locationUpdate(location: Coordinate) {}


    open fun latestLocation(coordinate: Coordinate) {}

    open fun showProgress(tag: String) {}

    open fun hideProgress(tag: String) {}

    open fun showError(tag: String, error: String) {}

    open fun isTablet(): Boolean {
        val screen = resources.configuration.screenLayout
        return screen and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    protected fun checkPermissionsGranted(permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) == PackageManager.PERMISSION_DENIED
            )
                return false
        }
        return true
    }

    protected fun permissionsRequest(
        permissions: Array<String>,
        granted: PermissionGranted? = null,
        denied: PermissionDenied? = null
    ) {
        var hasPermission: Boolean = true
        val deniedPermissions = mutableListOf<String>()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) == PackageManager.PERMISSION_DENIED
            ) {
                hasPermission = false
                deniedPermissions.add(permission)
            }
        }
        if (hasPermission) {
            granted?.let { it(permissions) }
        } else {
            denied?.let { it(deniedPermissions.toTypedArray()) }
            this.granted = granted
            this.denied = denied
            ActivityCompat.requestPermissions(this, permissions, permissionRequest)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionRequest) {
            if (permissions.size == grantResults.size) {
                if (granted != null)
                    granted!!(arrayOf(*permissions))
            } else {
                denied!!(arrayOf(*permissions))
            }
        }
    }
}