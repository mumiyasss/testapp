package com.example.android.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), PermissionHandler {

    //    val vm by viewModels<MainViewModel>()
    companion object {
        const val PERMISSION_REQUEST_CODE = 100
    }

    private var actionOnPermission: ((granted: Boolean) -> Unit) = {}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_restraunts.adapter = adapter
        rv_restraunts?.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )
        handlePermission(PERMISSION_LOCATION) {
            getGeoLocation()
        }
    }

    private val adapter = RestrauntsPagedListAdapter()
    private val vm by viewModels<MainViewModel>()

    private fun onGeoGet(location: Location) {
        vm.buildPagedList(location.latitude, location.longitude).observe(this) {
            adapter.submitList(it)
        }
//        vm.buildPagedList(40.7128, 74.0060).observe(this) {
//            adapter.submitList(it)
//        }
    }

    private val locationRequest by lazy {
        LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    private val locationCallback by lazy {
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult == null) {
                    return
                }
                for (location in locationResult.locations) {
//                    userLocationLayer.setObjectListener(locationListener)
                    onGeoGet(location)
                }
            }

        }
    }

    @SuppressLint("MissingPermission")
    private fun getGeoLocation() {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        onGeoGet(location)
                    } else {
                        fusedLocationClient.requestLocationUpdates(
                            locationRequest,
                            locationCallback,
                            Looper.getMainLooper()
                        )
                    }

                }
        }

    }


    /**
     * Для работы с Permissions.
     * @param permissionId - Android Permission Id.
     * @param callback - что нужно сделать, после предоставления прав доступа.
     */
    override fun handlePermission(permissionId: Int, callback: (granted: Boolean) -> Unit) {
        if (hasPermission(permissionId)) {
            callback(true)
        } else {
            actionOnPermission = callback
            ActivityCompat.requestPermissions(
                this,
                arrayOf(getPermissionString(permissionId)),
                MainActivity.PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MainActivity.PERMISSION_REQUEST_CODE && grantResults.isNotEmpty()) {
            actionOnPermission.invoke(grantResults[0] == PackageManager.PERMISSION_GRANTED)
        }
    }
}

interface PermissionHandler {
    fun handlePermission(permissionId: Int, callback: (granted: Boolean) -> Unit)
}


fun Context.hasPermission(permId: Int) =
    ContextCompat.checkSelfPermission(
        this,
        getPermissionString(permId)
    ) == PackageManager.PERMISSION_GRANTED

fun getPermissionString(id: Int) = when (id) {
    PERMISSION_LOCATION -> Manifest.permission.ACCESS_FINE_LOCATION
    else -> ""
}

const val PERMISSION_LOCATION = 3


const val token =
    "nHwOICuMpcv4zoj4kFkkzlarDPQf0vxgILtYOrjRft6eUgCE8DtzZuQ4oxOQqLyoi1n_qK0Hpp0V5yDI2cHVzC9PjGEfaY2zBrRTJD6SMZ45e9POnWrrm2pTmjBhWXYx"