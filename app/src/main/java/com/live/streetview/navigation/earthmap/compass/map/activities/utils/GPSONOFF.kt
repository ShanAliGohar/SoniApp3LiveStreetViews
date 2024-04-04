package com.live.streetview.navigation.earthmap.compass.map.activities.utils

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import androidx.appcompat.app.AlertDialog

class GPSONOFF {
    fun gpsONOFF(context: Context) {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            val alertDialog = AlertDialog.Builder(context)
            alertDialog.setTitle("Enable GPS")
            alertDialog.setMessage("Your GPS setting is not enabled. Please enabled it in settings menu.")
            alertDialog.setPositiveButton("Settings") { dialog, which ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                context.startActivity(intent)
            }
            alertDialog.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
            val alert = alertDialog.create()
            alert.show()
        }
    }
}
