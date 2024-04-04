package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.helper

import android.util.Log
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.helper.OsmHelper
import org.osmdroid.tileprovider.tilesource.MapBoxTileSource
import org.osmdroid.util.MapTileIndex

class OSMTileSourceFixed(str: String?, i: Int, i2: Int, i3: Int) :
    MapBoxTileSource(str, i, i2, i3, "") {


    override fun getTileURLString(j: Long): String {

        OsmHelper.accessToken
        Log.d("MapBoxLinkTAG", "getTileURLString: "+"https://api.mapbox.com/styles/v1/mapbox/" + "satellite-streets-v11" + "/tiles/" + MapTileIndex.getZoom(
            j) + "/" + MapTileIndex.getX(j) + "/" + MapTileIndex.getY(j) + "?access_token=" + OsmHelper.accessToken)
        return "https://api.mapbox.com/styles/v1/mapbox/" + "satellite-streets-v11"  + "/tiles/" + MapTileIndex.getZoom(
            j) + "/" + MapTileIndex.getX(j) + "/" + MapTileIndex.getY(j) + "?access_token=" + OsmHelper.accessToken
    }
}