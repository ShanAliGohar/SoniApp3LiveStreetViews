package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class StopServiceReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            val stopServiceIntent = Intent(it, AutoTripServices::class.java)
            stopServiceIntent.action = STOP_SERVICE_ACTION
            it.startService(stopServiceIntent)
        }
    }
}
