package com.streetview.liveearth.satellitemap.gpsnavigation.app.utilites
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log

import kotlinx.coroutines.*
import org.osmdroid.util.GeoPoint
import kotlin.coroutines.CoroutineContext

class ReverseGeocoderCloneCoroutineTask : CoroutineScope {

    private val TAG = "RCoroutineTask:"
    private var job: Job = Job()
    private var mContext: Context
    private var callBack: GeoTaskCallback
    private var latLngAddress: String

    constructor(mContext: Context, latLngAddress: String, callBack: GeoTaskCallback) {
        this.mContext = mContext
        this.callBack = callBack
        this.latLngAddress = latLngAddress
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
    private suspend fun doInBackground(): GeoPoint? =
        withContext(Dispatchers.IO) { // to run code in Background Thread
            Log.d(TAG, "doInBackground: ")
            val geocoderHelper = Geocoder(mContext)
            val listAddressed: ArrayList<Address?>
            var location: Address? = null
            var latLng: GeoPoint? = null
            if (Geocoder.isPresent()) {
                try {
                    listAddressed = geocoderHelper.getFromLocationName(
                        latLngAddress,
                        5
                    ) as java.util.ArrayList<Address?>
                    if (listAddressed.size > 0) {
                        location = listAddressed[0]
                        latLng = GeoPoint(
                            location!!.latitude,
                            location.longitude
                        )
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
            return@withContext latLng
        }

    // Runs on the Main(UI) Thread
    private fun onPreExecute() {
        Log.d(TAG, "onPreExecute: ")
        // show progress
    }


    // Runs on the Main(UI) Thread
    private fun onPostExecute(result: GeoPoint?) {
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
        fun onSuccessLocationFetched(fetchedLatLngs: GeoPoint?)
        fun onFailedLocationFetched()
    }

}