package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.api.IMapController
import org.osmdroid.tileprovider.MapTileProviderBasic
import org.osmdroid.tileprovider.tilesource.MapBoxTileSource
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class OsmHelper {
    companion object {

        var accessToken =
            "pk.eyJ1IjoibGl2ZWR1Yjc3IiwiYSI6ImNsOGlvcXB4czAzMDQzcHAyNmV3bTQ2ZnQifQ.LBetyiSwzmKWFCx7DNsW1Q"

        private const val bingMapApiKey =
            "AiHpeKq3PsuJTilRM7Cwna2MJ9ER69nz4fTXi5U3J1l2FrIxhXwGyhFi-aPbLv8w"
        //old
//        const val bindMapApiKey = "AoexEyqVex1qAdw1WdPm-gAot8bO_-Tf6B-5ZfH5jWGaOc7Q_0GSgy6ZilW2HPWn"

        fun zoomWithAnimate(mController: IMapController, point: GeoPoint, zoom: Int) {
            mController.animateTo(point, zoom.toDouble(), 4000, 2F, false)
        }



        fun setStdTileProvider(context: Context, mMapView: MapView) {
            if (mMapView.tileProvider !is MapTileProviderBasic) {
                val bitmapProvider = MapTileProviderBasic(context)
                mMapView.tileProvider = bitmapProvider
            }
        }

        fun setMarkerIconAsPhoto(context: Context, marker: Marker, thumbnail: Bitmap) {
            var thumbnail = thumbnail
            val borderSize = 2
            thumbnail = Bitmap.createScaledBitmap(thumbnail, 100, 100, true)
            val withBorder = Bitmap.createBitmap(
                thumbnail.width + borderSize * 2,
                thumbnail.height + borderSize * 2,
                thumbnail.config
            )
            val canvas = Canvas(withBorder)
            canvas.drawColor(Color.TRANSPARENT)
            canvas.drawBitmap(thumbnail, borderSize.toFloat(), borderSize.toFloat(), null)
            val icon = BitmapDrawable(context.resources, withBorder)
            marker.icon = icon
        }

        private fun setMarkerIconAsOnMap(context: Context, marker: Marker, thumbnail: Bitmap) {
            var thumbnail = thumbnail
            val borderSize = 2
            thumbnail = Bitmap.createScaledBitmap(thumbnail, 70, 70, true)
            val withBorder = Bitmap.createBitmap(
                thumbnail.width + borderSize * 2,
                thumbnail.height + borderSize * 2,
                thumbnail.config
            )
            val canvas = Canvas(withBorder)
            canvas.drawColor(Color.TRANSPARENT)
            canvas.drawBitmap(thumbnail, borderSize.toFloat(), borderSize.toFloat(), null)
            val icon = BitmapDrawable(context.resources, withBorder)
            marker.icon = icon
        }


        fun addMarkerOnMapBitmapWithName(
            context: Context,
            mOriginPoint: GeoPoint,
            titleName: String,
            bitmap: Bitmap,
            mapView: MapView
        ) {
            try {
                val startMarker = Marker(mapView)
                startMarker.position = mOriginPoint
                startMarker.title = titleName
                startMarker.icon = null
                setMarkerIconAsOnMap(context, startMarker, bitmap)
                mapView.overlays.add(startMarker)
            } catch (e: Exception) {
            }
        }

        fun setMapBoxMapStyleLayer(context: Context): OnlineTileSourceBase {
            var mMabBoxStyle: OnlineTileSourceBase? = null

            mMabBoxStyle = MapBoxTileSource("MapBoxSatelliteLabelled", 1, 19, 256, ".png")
            mMabBoxStyle.retrieveAccessToken(context)
            mMabBoxStyle.retrieveMapBoxMapId(context)
            return TileSourceFactory.addTileSource(mMabBoxStyle) as OnlineTileSourceBase
        }

        private fun hav(x: Double): Double {
            val sinHalf = sin(x * 0.5)
            return sinHalf * sinHalf
        }

        private fun arcHav(x: Double): Double {
            return 2.0 * asin(sqrt(x))
        }

        private fun havDistance(lat1: Double, lat2: Double, dLng: Double): Double {
            return hav(lat1 - lat2) + hav(dLng) * cos(lat1) * cos(lat2)
        }

        private fun distanceRadians(
            lat1: Double,
            lng1: Double,
            lat2: Double,
            lng2: Double
        ): Double {
            return arcHav(havDistance(lat1, lat2, lng1 - lng2))
        }

        private fun computeAngleBetween(from: GeoPoint, to: GeoPoint): Double {
            return distanceRadians(
                Math.toRadians(from.latitude),
                Math.toRadians(from.longitude),
                Math.toRadians(to.latitude),
                Math.toRadians(to.longitude)
            )
        }

        fun computeDistanceBetween(from: GeoPoint, to: GeoPoint): Double {
            return computeAngleBetween(from, to) * 6371009.0
        }
    }
}