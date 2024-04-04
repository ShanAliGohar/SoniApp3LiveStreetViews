package com.live.streetview.navigation.earthmap.compass.map.Model

class IsoModel {
    var name: String? = null
    var image: String? = null
    var countrycodes: String? = null
        set(countrycodes) {
            field = this.countrycodes
        }
    var countryCodeNumber: String? = null
    var countryCodeAlpha: String? = null
    var area: String? = null
    var flagUrl: String? = null
    var capital: String? = null
    var latlng: String? = null
    var currency: String? = null
    var population: String? = null
    var timezones: String? = null
    var region: String? = null
    var isSelected = false
}
