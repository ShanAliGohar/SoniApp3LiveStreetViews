package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.utils


import android.content.Context
import android.content.SharedPreferences

class SharedPrefManager constructor(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)


    fun setActiveVehicleId(vehicleId: Long) {
        sharedPreferences.edit().putLong(KEY_ACTIVE_VEHICLE_ID, vehicleId).apply()
    }

    fun getActiveVehicleId(): Long {
        return sharedPreferences.getLong(KEY_ACTIVE_VEHICLE_ID, 0)
    }

    // Corrected key for default currency
    fun setDefaultCurrency(defaultCurrency: String) {
        sharedPreferences.edit().putString(USER_DEFAULT_CURRENCY_SYMBOL, defaultCurrency).apply()
    }

    // Corrected key for default currency
    fun getDefaultCurrency(): String {
        return sharedPreferences.getString(USER_DEFAULT_CURRENCY_SYMBOL, "") ?: ""
    }



    companion object {
        private const val PREF_NAME = "MyAppPreferences"
        private const val KEY_ACTIVE_VEHICLE_ID = "activeVehicleId"
        private const val USER_DEFAULT_CURRENCY_SYMBOL = "currencySymbol"

        private var instance: SharedPrefManager? = null

        fun getInstance(context: Context): SharedPrefManager {
            if (instance == null) {
                synchronized(SharedPrefManager::class.java) {
                    if (instance == null) {
                        instance = SharedPrefManager(context.applicationContext)
                    }
                }
            }
            return instance!!
        }
    }
}
