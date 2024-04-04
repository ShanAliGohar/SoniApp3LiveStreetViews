package com.live.streetview.navigation.earthmap.compass.map.places

import com.live.streetview.navigation.earthmap.compass.map.places.Geometry
import com.live.streetview.navigation.earthmap.compass.map.places.Properties

data class Feature(
    val geometry: Geometry,
    val properties: Properties,
    val type: String
)