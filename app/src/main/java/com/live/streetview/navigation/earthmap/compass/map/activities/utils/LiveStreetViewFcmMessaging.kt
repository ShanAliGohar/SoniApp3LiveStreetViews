package com.live.streetview.navigation.earthmap.compass.map.activities.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.live.streetview.navigation.earthmap.compass.map.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class LiveStreetViewFcmMessaging : FirebaseMessagingService() {
    private var titleLiveStreetView: String? = null
    private var bodyLiveEarthMapStreetLive: String? = null
    private var descriptionLiveStreetView: String? = null
    private var iconLiveStreetView: String? = null
    private var bitmapLiveStreetView: Bitmap? = null
    private var bitIconLiveStreetView: Bitmap? = null
    private val CHANNEL_ID_MY_FCM_LiveStreetView: String = "id_channel"
    private val CHANNEL_NAME_MY_FCM_LiveStreetView: String = "name_channel"
    private val CHANNEL_DESCRIPTION_MY_FCM_LiveStreetView: String = "description_channel"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.i("onMessageReceived", "MessageReceived:")
        try {
            titleLiveStreetView = remoteMessage!!.data["title"]
            bodyLiveEarthMapStreetLive = remoteMessage.data["app_url"]
            descriptionLiveStreetView = remoteMessage.data["short_desc"]
            iconLiveStreetView = remoteMessage.data["icon"]
            bitIconLiveStreetView = getBitmapImageFromRemoteUrlLiveStreetView(iconLiveStreetView!!)
            bitmapLiveStreetView =
                getBitmapImageFromRemoteUrlLiveStreetView(remoteMessage.data["feature"]!!)
            Log.d("fcm", "onMessageReceived: ")
        } catch (e: Exception) {
            Log.d("fcm", "Error : " + e.localizedMessage)
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val mNotificationManagerLiveEarthMap =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val importanceLiveEarthMap = NotificationManager.IMPORTANCE_HIGH
            val mChannelLiveEarthMap =
                NotificationChannel(
                    CHANNEL_ID_MY_FCM_LiveStreetView,
                    CHANNEL_NAME_MY_FCM_LiveStreetView,
                    importanceLiveEarthMap

                )

            mChannelLiveEarthMap.description = CHANNEL_DESCRIPTION_MY_FCM_LiveStreetView
            mChannelLiveEarthMap.enableLights(true)
            mChannelLiveEarthMap.lightColor = Color.BLUE
            mChannelLiveEarthMap.enableVibration(true)
            mChannelLiveEarthMap.vibrationPattern =
                longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            try {
                mNotificationManagerLiveEarthMap.createNotificationChannel(mChannelLiveEarthMap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            fcmNotificationLiveStreetView()
        }


    }

    private fun fcmNotificationLiveStreetView() {
        val bigViewLiveEarthMap =
            RemoteViews(packageName, R.layout.earth_live_map_fcm_notification_large)
        val smallViewLiveEarthMap =
            RemoteViews(packageName, R.layout.earth_live_map_fcm_notification_small)
        bigViewLiveEarthMap.setTextViewText(R.id.text, descriptionLiveStreetView)
        smallViewLiveEarthMap.setTextViewText(R.id.text, descriptionLiveStreetView)
        bigViewLiveEarthMap.setTextViewText(R.id.titlePlaceName, titleLiveStreetView)
        smallViewLiveEarthMap.setTextViewText(R.id.titlePlaceName, titleLiveStreetView)
        smallViewLiveEarthMap.setImageViewBitmap(R.id.image_app, bitIconLiveStreetView)
        bigViewLiveEarthMap.setImageViewBitmap(R.id.image_app, bitIconLiveStreetView)
        bigViewLiveEarthMap.setImageViewBitmap(R.id.image_pic, bitmapLiveStreetView)

        val notificationCompLiveEarthMap =
            NotificationCompat.Builder(this, CHANNEL_ID_MY_FCM_LiveStreetView)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setCustomBigContentView(bigViewLiveEarthMap)
                .setCustomContentView(smallViewLiveEarthMap)

        val resultIntentLiveEarthMap = if (!bodyLiveEarthMapStreetLive.isNullOrBlank()) {
            Intent(Intent.ACTION_VIEW, Uri.parse(bodyLiveEarthMapStreetLive))
        } else {
            Log.e("LiveStreetViewFcMessage", "bodyLiveEarthMapStreetLive is null or blank")
            null
        }

        if (resultIntentLiveEarthMap != null) {
            resultIntentLiveEarthMap.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

            val pendingIntLiveEarthMap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.getActivity(
                    this,
                    0,
                    resultIntentLiveEarthMap,
                    PendingIntent.FLAG_IMMUTABLE
                )
            } else {
                PendingIntent.getActivity(
                    this,
                    0,
                    resultIntentLiveEarthMap,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            }

            notificationCompLiveEarthMap.setContentIntent(pendingIntLiveEarthMap)
            val notificaitonManagerLiveEarthMap = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificaitonManagerLiveEarthMap.notify(1, notificationCompLiveEarthMap.build())
        } else {
            Log.w("LiveStreetViewFcMessage", "Ignoring notification due to null or blank bodyLiveEarthMapStreetLive")

        }

//        val resultIntentLiveEarthMap = Intent(Intent.ACTION_VIEW, Uri.parse(bodyLiveEarthMapStreetLive))
//        resultIntentLiveEarthMap.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//        resultIntentLiveEarthMap.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
//
//        val pendingIntLiveEarthMap =
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                PendingIntent.getActivity(
//                    this,
//                    0,
//                    resultIntentLiveEarthMap,
//                    PendingIntent.FLAG_IMMUTABLE
//                )
//            } else {
//                PendingIntent.getActivity(
//                    this,
//                    0,
//                    resultIntentLiveEarthMap,
//                    PendingIntent.FLAG_UPDATE_CURRENT
//                )
//            }
//
//        notificationCompLiveEarthMap.setContentIntent(pendingIntLiveEarthMap)
//        val notificaitonManagerLiveEarthMap =
//            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificaitonManagerLiveEarthMap.notify(1, notificationCompLiveEarthMap.build())
    }

    private fun getBitmapImageFromRemoteUrlLiveStreetView(imageUrl: String): Bitmap? {
        try {
            val urlLiveEarthMap = URL(imageUrl)
            val connectionLiveEarthMap = urlLiveEarthMap.openConnection() as HttpURLConnection
            connectionLiveEarthMap.doInput = true
            connectionLiveEarthMap.connect()
            var inputLiveEarthMap: InputStream? = null
            try {
                inputLiveEarthMap = connectionLiveEarthMap.inputStream
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return BitmapFactory.decodeStream(inputLiveEarthMap)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

    }
}