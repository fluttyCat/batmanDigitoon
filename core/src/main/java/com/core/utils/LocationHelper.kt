package com.core.utils


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.core.dto.Coordinate
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import ir.co.rayanpay.common.dto.LocationState
import java.lang.ref.WeakReference


interface LocationHelper {
    fun getLocationState()            : MutableLiveData<LocationState>
    fun onLocationUpdate()            : MutableLiveData<Coordinate>

    fun locationPermissionRequired() : MutableLiveData<ArrayList<String>>

    fun requestStartUpdatingLocation(activity: WeakReference<Activity>, requestEnable: Boolean? = true, fastInterval: Long? = 1 * 1000, interval : Long? = 1 * 1000)
    fun requestStopUpdatingLocation()

    fun onPermissionGranted(activity: WeakReference<Activity>)
}

class LocationHelperImpl : LocationHelper, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private lateinit var fused: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    val onLocation: MutableLiveData<Coordinate> = MutableLiveData()
    val onStatus: MutableLiveData<LocationState> = MutableLiveData()
    private val onPermissionRequest: MutableLiveData<ArrayList<String>> = MutableLiveData()

    companion object {
        const val LOCATION_SETTING_REQUEST = 1002
    }

    override fun getLocationState(): MutableLiveData<LocationState> = onStatus
    override fun onLocationUpdate(): MutableLiveData<Coordinate> = onLocation
    override fun locationPermissionRequired(): MutableLiveData<ArrayList<String>> = onPermissionRequest

    override fun requestStartUpdatingLocation(activity: WeakReference<Activity>, requestEnable: Boolean?, fastInterval: Long?, interval: Long?) {
        try {
            if (isPermissionGranted(activity) && isLocationEnable(activity, requestEnable != false)) {
                createLocationRequest(activity, fastInterval, interval)
            }
        } catch (e: Exception) {
            onStatus.postValue(LocationState.error(e.message))
        }
    }
    override fun requestStopUpdatingLocation() {
        if (::fused.isInitialized) {
            fused.removeLocationUpdates(locationCallback)
        }
    }
    override fun onPermissionGranted(activity: WeakReference<Activity>) {
        requestStartUpdatingLocation(activity)
    }

    private fun isPermissionGranted(activity: WeakReference<Activity>): Boolean {
        try {
            val permissions = ArrayList<String>()
            if (activity.get() != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(
                            activity.get()!!,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                }
            }
            return if (permissions.size > 0) {
                onPermissionRequest.postValue(permissions)
                false
            } else {
                true
            }
        } catch (e: Exception) {
            onStatus.postValue(LocationState.error(e.message))
            return false
        }
    }
    private fun isLocationEnable(activity: WeakReference<Activity>, requestEnable: Boolean): Boolean {
        return try {
            val manager =
                activity.get()?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                true
            } else {
                if (requestEnable)
                    requestEnableLocation(activity)
                false
            }
        } catch (e: Exception) {
            onStatus.postValue(LocationState.error(e.message))
            false
        }
    }
    private fun requestEnableLocation(activity: WeakReference<Activity>) {
        activity.get()?.let {
            try {
                val locationRequest = LocationRequest.create()
                locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

                val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest).setAlwaysShow(true)
                val task = LocationServices.getSettingsClient(it).checkLocationSettings(builder.build())
                task.addOnSuccessListener {
                    requestStartUpdatingLocation(activity, false)
                }
                task.addOnFailureListener { e ->
                    try {
                        if (e is ResolvableApiException) {
                            try {
                                e.startResolutionForResult(it, LOCATION_SETTING_REQUEST)
                            } catch (sendEx: IntentSender.SendIntentException) {
                                onStatus.postValue(LocationState.error(e.message))
                            }
                        }
                    } catch (e: Exception) {
                        onStatus.postValue(LocationState.error(e.message))
                    }
                }
            } catch (e: Exception) {
                onStatus.postValue(LocationState.error(e.message))
            }
        }
    }
    private fun createLocationRequest(activity: WeakReference<Activity>, fastInterval: Long? = 1000, interval: Long? = 1000) {
        try {
            onStatus.postValue(LocationState.LOADING)
            val request = LocationRequest.create()
            request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            request.interval = interval ?: 1000
            request.fastestInterval = fastInterval ?: 1000
            fused = LocationServices.getFusedLocationProviderClient(activity.get()!!)
            fused.lastLocation.addOnSuccessListener {
                try {
                    onStatus.postValue(LocationState.LOADED)
                    if (it != null) {
                        onLocation.postValue(Coordinate(it.latitude, it.longitude))
                    }
                } catch (e: Exception) {
                    onStatus.postValue(LocationState.error(e.message))
                }
            }
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    try {
                        onStatus.postValue(LocationState.LOADED)
                        if (locationResult == null) {
                            onLocation.postValue(Coordinate(0.0, 0.0))
                            return
                        }

                        for (newLocation in locationResult.locations) {
                            onLocation.postValue(Coordinate(newLocation.latitude, newLocation.longitude))
                        }
                    } catch (e: Exception) {
                        onStatus.postValue(LocationState.error(e.message))
                    }
                }
            }

            fused.requestLocationUpdates(request, locationCallback, Looper.myLooper())
        } catch (e: Exception) {
            onStatus.postValue(LocationState.error(e.message))
        }
    }

    override fun onConnected(p0: Bundle?) {}
    override fun onConnectionSuspended(p0: Int) {
        onStatus.postValue(LocationState.error("onConnectionSuspended"))
    }
    override fun onConnectionFailed(p0: ConnectionResult) {
        onStatus.postValue(LocationState.error("onConnectionFailed"))
    }
}