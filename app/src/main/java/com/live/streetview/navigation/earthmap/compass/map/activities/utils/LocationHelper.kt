package com.live.streetview.navigation.earthmap.compass.map.activities.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocationHelper {
    companion object{

        suspend fun isProviderEnabled(mContext: Context){
            Log.i("getLocation","isProviderEnabled :")
            val locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                islocationPerMisstionProvided(mContext)
            }else{
                withContext(Dispatchers.Main) {
                    showGpsLocationEnabledDialog(mContext)
                }
            }
        }

        fun showGpsLocationEnabledDialog(mContext: Context) {
            Log.i("getLocation", "showGpsLocationEnabledDialog :")
//            val gpsEnablerDialog = GpsEnablerDialog(mContext)
//            gpsEnablerDialog.show()
        }

        suspend fun islocationPerMisstionProvided(mContext: Context):Boolean {
            Log.i("getLocation","islocationPerMisstionProvided :")
            if (Build.VERSION.SDK_INT >= 23) {
                if (ActivityCompat.checkSelfPermission(
                        mContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(
                        mContext,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    try {
                        ActivityCompat.requestPermissions(
                            mContext as Activity,
                            arrayOf(
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ), 1
                        )
                    } catch (e: Exception) {
                    }
                    return false
                }else{
                    return true
                }
            }else{
                return true
            }
        }

        fun isInternetAvailable(mContext: Context): Boolean {
            val connectivityManager =
                mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var activeNetworkInfo: NetworkInfo? = null
            try {
                activeNetworkInfo = connectivityManager.activeNetworkInfo
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }


        fun islocationPerMisstionProvidedCheciker(mContext: Context):Boolean {
            Log.i("getLocation","islocationPerMisstionProvided :")
            if (Build.VERSION.SDK_INT >= 23) {
                if (ActivityCompat.checkSelfPermission(
                        mContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(
                        mContext,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    try {
                        ActivityCompat.requestPermissions(
                            mContext as Activity,
                            arrayOf(
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ), 1
                        )
                    } catch (e: Exception) {
                    }

                    return false


                }else{
                    return true
                }
            }else{
                return true
            }
        }
        fun checkGps(mContext: Context) {
            val locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showGpsLocationEnabledDialog(mContext)
            }
        }


    }





}