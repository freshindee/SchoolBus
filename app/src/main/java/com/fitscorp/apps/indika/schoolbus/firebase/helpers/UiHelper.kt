package com.fitscorp.apps.indika.schoolbus.helpers

import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.afollestad.materialdialogs.MaterialDialog
import com.fitscorp.apps.indika.schoolbus.R
import com.fitscorp.apps.indika.schoolbus.interfaces.IPositiveNegativeListener


import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.LocationRequest


class UiHelper {

    fun isPlayServicesAvailable(context: Context): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val status = googleApiAvailability.isGooglePlayServicesAvailable(context)
        return true
    }

    fun isHaveLocationPermission(context: Context): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    fun isLocationProviderEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

//    fun showPositiveDialogWithListener(callingClassContext: Context, title: String, content: String, positiveNegativeListener: IPositiveNegativeListener, positiveText: String, cancelable: Boolean) {
////        buildDialog(callingClassContext, title, content)
////                .builder
////                .positiveText(positiveText)
////                .positiveColor(getColor(R.color.colorPrimary, callingClassContext))
////                .onPositive { _, _ -> positiveNegativeListener.onPositive() }
////                .cancelable(cancelable)
////                .show()
//    }

//    private fun buildDialog(callingClassContext: Context, title: String, content: String): MaterialDialog {
//
////        return MaterialDialog.Builder(callingClassContext)
////                .title(title)
////                .content(content)
////                .build()
//    }


    private fun getColor(color: Int, context: Context): Int {
        return ContextCompat.getColor(context, color)
    }

    fun getLocationRequest(): LocationRequest {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 3000
        return locationRequest
    }
}