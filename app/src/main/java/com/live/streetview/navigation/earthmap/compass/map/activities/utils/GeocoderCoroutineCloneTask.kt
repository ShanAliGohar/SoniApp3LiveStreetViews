package com.streetview.liveearth.satellitemap.gpsnavigation.app.utilites

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import kotlinx.coroutines.*
import org.osmdroid.util.GeoPoint


import kotlin.coroutines.CoroutineContext


// implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")

class GeocoderCoroutineCloneTask : CoroutineScope {

    private val TAG = "CoroutineTask:"
    private var job: Job = Job()
    private var mContext: Context
    private var callBack: GeoTaskCallback
    private var latLng: GeoPoint

    constructor(mContext: Context, latLng: GeoPoint, callBack: GeoTaskCallback) {
        this.mContext = mContext
        this.callBack = callBack
        this.latLng = latLng
    }
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    fun execute() = launch { /*launch is having main thread scope*/
        Log.d(TAG, "execute:")
        onPreExecute()
        val result = doInBackground() // runs in background thread without blocking the Main Thread
        onPostExecute(result)
    }

    /*it will done then next will call*/
    private suspend fun doInBackground(): String =
        withContext(Dispatchers.IO) { // to run code in Background Thread
            Log.d(TAG, "doInBackground: ")
            // delay(1000)
            var location = "User Location"
            val geocoderHelper = Geocoder(mContext)
            val listAddressed: ArrayList<Address>
            if (Geocoder.isPresent()) {
                try {
                    listAddressed = geocoderHelper.getFromLocation(
                        latLng.latitude,
                        latLng.longitude,
                        1
                    ) as ArrayList<Address>
                    if (listAddressed.size > 0) {
                        location = listAddressed[0].getAddressLine(0)
                    }
                } catch (e: Exception) {
                }
            }
            return@withContext location
        }

    // Runs on the Main(UI) Thread
    private fun onPreExecute() {
        Log.d(TAG, "onPreExecute: ")
        // show progress
    }


    // Runs on the Main(UI) Thread
    private fun onPostExecute(result: String) {
        Log.d(TAG, "onPostExecute: Result: $result")
        callBack.onSuccessLocationFetched(result)
        cancel()
    }


    // call this method to cancel a coroutine when you don't need it anymore,
    // e.g. when user closes the screen
    private fun cancel() {
        Log.d(TAG, "cancel")
        job.cancel()
    }


    interface GeoTaskCallback {
        fun onSuccessLocationFetched(fetchedAddress: String?)
        fun onFailedLocationFetched()
    }

}