package com.core.base


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.core.dto.Coordinate
import com.core.dto.Status
import ir.co.rayanpay.common.dto.LocationState
import java.lang.ref.WeakReference

abstract class ParentFragment<T : BaseViewModel, E : ViewDataBinding> : BaseFragment() {

    lateinit var viewModel: T

    lateinit var dataBinding: E

    abstract fun getViewModelClass(): Class<T>

    abstract fun getFactory(): ViewModelProvider.Factory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        viewModel = ViewModelProvider(this, getFactory()).get(getViewModelClass())


        viewModel.getNetworkStatus().observe(this, Observer { it ->
            when (it.status) {
                Status.RUNNING -> showProgress(it.tag ?: "")
                Status.SUCCESS -> hideProgress(it.tag ?: "")
                else -> {
                    showError(
                        it.tag ?: "",
                        it.msg ?: getString(it.event),
                        it.code,
                        it.errorBody
                    )

                }
            }

            it.tag?.takeIf { string ->
                string.contains("_getRefreshToken")
            }?.let {
                return@Observer
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
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            getResourceLayoutId(),
            container,
            false
        )
        lifecycle.addObserver(viewModel)
        viewModel.onCreated()
        return dataBinding.root
    }

    open fun locationState(state: LocationState) {}

    open fun locationUpdate(location: Coordinate) {}

    private fun requestStartUpdatingLocation(requestEnableSetting: Boolean? = true) {
        viewModel.requestStartUpdatingLocation(
            WeakReference(requireActivity()),
            requestEnableSetting
        )
    }

    private fun requestStopUpdatingLocation() {
        viewModel.requestStopUpdatingLocation()
    }


}