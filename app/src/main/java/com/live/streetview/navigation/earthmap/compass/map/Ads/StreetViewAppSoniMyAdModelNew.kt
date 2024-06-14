package com.live.streetview.navigation.earthmap.compass.map.Ads

class StreetViewAppSoniMyAdModelNew {
    var appid_admob_inApp: String? = null
    var banner_admob_inApp: String? = null
    var interstitial_admob_inApp: String? = null
    var native_admob_inApp: String? = null
    var isShould_show_admob = false
    var isCtr_control = false
    var next_ads_time = 0.0
    var current_counter = 0.0
    var app_open_admob_inApp: String? = null
    var isShould_show_app_open = false
    var isIs_splash_show = false
        private set
    var adShowAfter = 0

    constructor() {}
    constructor(
        appid_admob_inApp: String?, banner_admob_inApp: String?, interstitial_admob_inApp: String?,
        native_admob_inApp: String?, should_show_admob: Boolean, ctr_control: Boolean,
        next_ads_time: Double, current_counter: Double, app_open_admob_inApp: String?,
        should_show_app_open: Boolean,
        is_splash_show: Boolean, adShowAfter: Int
    )
    {
        this.appid_admob_inApp = appid_admob_inApp
        this.banner_admob_inApp = banner_admob_inApp
        this.interstitial_admob_inApp = interstitial_admob_inApp
        this.native_admob_inApp = native_admob_inApp
        isShould_show_admob = should_show_admob
        isCtr_control = ctr_control
        this.next_ads_time = next_ads_time
        this.current_counter = current_counter
        this.app_open_admob_inApp = app_open_admob_inApp
        isShould_show_app_open = should_show_app_open
        isIs_splash_show = is_splash_show
        this.adShowAfter = adShowAfter
    }

    fun setIs_splash_show(is_splash_show: Boolean) {
        isIs_splash_show = is_splash_show
    }
}