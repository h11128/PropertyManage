package com.jason.propertymanager.ui.property

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.jason.propertymanager.other.tag_d
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.util.*

class LocationRepository(var mContext: Activity) {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    val REQUEST_CHECK_SETTINGS = -5
    var callback: LocationResultCallBack? = null
    val begin = 0
    val finish = 1
    val locationCallback: LocationCallback = object: LocationCallback() {
        override fun onLocationResult(p0: LocationResult?) {
            super.onLocationResult(p0)
            if (p0 == null || p0.locations.size < 0) {
                return
            }
            else {
                Log.d("abc", "get ${p0.locations.size} location")
                val location = p0.locations[p0.locations.size - 1]
                fusedLocationClient.removeLocationUpdates(this)
                callback?.update(location)

            }
        }
    }

    interface LocationResultCallBack{
        fun update(result: Location? = null)
    }
    
    val locationRequest: LocationRequest? = LocationRequest.create()?.apply {
        interval = 10000
        fastestInterval = 300
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
    fun requestLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext)

        checkLocationSetting(locationRequest)
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            callback?.update(null)
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }
        else {
            checkPermission()
        }
    }

    private fun checkLocationSetting(locationRequest: LocationRequest?) {

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest ?: return)

        val client: SettingsClient = LocationServices.getSettingsClient(mContext)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener { locationSettingsResponse ->
            Log.d(tag_d, "location setting satisfied ${locationSettingsResponse.locationSettingsStates}")
        }
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(mContext, REQUEST_CHECK_SETTINGS)
                }
                catch (sendEx: IntentSender.SendIntentException) {
                    Log.d(tag_d, "error ${sendEx.message}")
                }
            }
        }
    }

    private fun checkPermission() {
        Dexter.withContext(mContext)
            .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            .withListener(object: MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    if ((p0 ?: return).areAllPermissionsGranted()) {
                        Toast.makeText(mContext, "Permission granted", Toast.LENGTH_SHORT).show()
                        requestLocation()
                    }
                    else if (p0.isAnyPermissionPermanentlyDenied) {
                        Toast.makeText(mContext, "Need Permission to get location", Toast.LENGTH_SHORT).show()
                        val builder = AlertDialog.Builder(mContext)
                        builder.setMessage("Permission is needed to get location")
                        builder.setPositiveButton("Go to Setting") { dialog, _ ->
                            dialog.dismiss()
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = Uri.fromParts("package", mContext?.packageName, null)
                            intent.data = uri
                            mContext.startActivity(intent)
                        }
                        builder.setNegativeButton("Cancel") { dialog, _ ->
                            dialog.dismiss()
                        }
                        builder.show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(p0: MutableList<PermissionRequest>?, p1: PermissionToken?) {
                    p1?.continuePermissionRequest()
                }
            })
            .onSameThread()
            .check()
    }

}