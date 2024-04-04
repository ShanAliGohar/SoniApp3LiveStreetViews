package com.live.streetview.navigation.earthmap.compass.map.gdpr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.activities.SplashActivity

class ConsentActivity : AppCompatActivity() {
    lateinit var consentInformation: ConsentInformation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consent)

        //.setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)

       /*  val debugSettings = ConsentDebugSettings.Builder(this)
             .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
             .addTestDeviceHashedId("   ")
             .build()*/

        val params = ConsentRequestParameters
            .Builder()
            /* .setConsentDebugSettings(debugSettings)*/
            .build()

        consentInformation = UserMessagingPlatform.getConsentInformation(this)
        //consentInformation.reset()
        consentInformation.requestConsentInfoUpdate(
            this,
            params,
            {
                //success
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(this) { loadAndShowError ->
                    if (loadAndShowError != null) {
                        Log.d("consentInformation", "success: ${loadAndShowError.message}")
                        moveToNext()
                    }

                    if (consentInformation.canRequestAds()) {
                        Log.d("consentInformation", "canRequestAds")
                        moveToNext()
                    }else{
                        moveToNext()
                    }
                }
            },
            {
                //failure
                Log.d("consentInformation", "failure: ${it.message}")
                moveToNext()
            }
        )

    }

    private fun moveToNext() {
        val intent= Intent(this,SplashActivity::class.java)
        startActivity(intent)
        finish()
    }
}