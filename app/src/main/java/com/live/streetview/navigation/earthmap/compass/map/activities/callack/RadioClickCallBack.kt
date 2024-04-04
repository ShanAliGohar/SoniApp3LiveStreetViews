package com.live.streetview.navigation.earthmap.compass.map.activities.callack

interface RadioClickCallBack {
    fun onClickListener(pos: Int)
    fun callBackForPermissionChecking(type: String?)
}
