package com.live.streetview.navigation.earthmap.compass.map.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.RetrofitInfoModel.AllCountryDataModl
import de.hdodenhof.circleimageview.CircleImageView

class CustomSpinnerAdapterFlag(
    var context: Context,
    allCountryDataModlArrayList: ArrayList<AllCountryDataModl?>
) : BaseAdapter() {
    var allCountryDataModlArrayList = ArrayList<AllCountryDataModl?>()
    var inflter: LayoutInflater

    init {
        this.allCountryDataModlArrayList = allCountryDataModlArrayList
        inflter = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return allCountryDataModlArrayList.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, view: View, viewGroup: ViewGroup): View? {
        var view = view
        val model = allCountryDataModlArrayList[position]
        val flags = ("https://flagpedia.net/data/flags/normal/"
                + model?.alpha2Code?.lowercase() + ".png")
        view = inflter.inflate(R.layout.custom_fm_layout, null)
        val icon = view.findViewById<CircleImageView>(R.id.imageCircle)
        val names = view.findViewById<View>(R.id.txtcircle) as TextView
        names.text = allCountryDataModlArrayList[position]!!.alpha3Code
        Glide.with(context).load(flags).listener(object : RequestListener<Drawable?> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any,
                target: Target<Drawable?>,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any,
                target: Target<Drawable?>,
                dataSource: DataSource,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }
        }).into(icon)
        return null
    }
}
