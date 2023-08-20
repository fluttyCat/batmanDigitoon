package com.core.base

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.core.dto.ErrorResultDto


abstract class BaseFragment : Fragment() {

    val permissionRequest: Int = 12000

    var granted: PermissionGranted? = null

    var denied: PermissionDenied? = null

    abstract fun getResourceLayoutId(): Int


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
    }

    open fun hideKeyboard(view: View) {
        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    open fun isTablet(): Boolean {
        val screen = resources.configuration.screenLayout
        return screen and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    open fun checkPermissionsGranted(permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    context!!,
                    permission
                ) == PackageManager.PERMISSION_DENIED
            )
                return false
        }
        return true
    }

    open fun permissionsRequest(
        permissions: Array<String>,
        granted: PermissionGranted,
        denied: PermissionDenied
    ) {
        var hasPermission: Boolean = true
        val deniedPermissions = mutableListOf<String>()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    context!!,
                    permission
                ) == PackageManager.PERMISSION_DENIED
            ) {
                hasPermission = false
                deniedPermissions.add(permission)
            }
        }
        if (hasPermission) {
            granted(permissions)
        } else {
            denied(deniedPermissions.toTypedArray())
            this.granted = granted
            this.denied = denied
            requestPermissions(permissions, permissionRequest)
        }
    }

    open fun showMessage(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    open fun showProgress(tag: String) {}

    open fun hideProgress(tag: String) {}

    open fun backStack(tag: String) {}

    open fun showError(tag: String?, error: String?, code: Int?, errorBody: ErrorResultDto?) {}

    //open fun refreshToken(code: Int?) {}

    open fun inject() {}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionRequest) {
            if (permissions.size == grantResults.size) {
                granted!!(arrayOf(*permissions))
            } else {
                denied!!(arrayOf(*permissions))
            }
        }
    }
}