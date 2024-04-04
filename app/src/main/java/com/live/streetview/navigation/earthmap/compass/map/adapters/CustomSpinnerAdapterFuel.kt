package com.live.streetview.navigation.earthmap.compass.map.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
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
import kotlinx.coroutines.NonDisposableHandle.parent
import java.util.zip.Inflater

/*class CustomSpinnerAdapterFuel(var context: Context, flags: ArrayList<AllCountryDataModl?>) :
    BaseAdapter() {
    var streetViewCountryInfoModelArrayList = ArrayList<AllCountryDataModl?>()

    init {
        streetViewCountryInfoModelArrayList = flags
        
    }

    override fun getCount(): Int {
        return streetViewCountryInfoModelArrayList.size
    }
    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, view: View, parent: ViewGroup): View {
        var mview = view
        mview = LayoutInflater.from(context).inflate(R.layout.custom_spinner_layout,parent,false)
        mview?.let { itemView ->
            val model = streetViewCountryInfoModelArrayList[position]
            val flags = ("https://flagpedia.net/data/flags/normal/"
                    + model?.toString()?.lowercase() + ".png")

            val icon = itemView.findViewById<CircleImageView>(R.id.customSpinnerImage)
            val names = itemView.findViewById(R.id.customSpinnerTV) as TextView
            names.text = streetViewCountryInfoModelArrayList[position]?.toString()

            Glide.with(context)
                .load(flags)
                .listener(object : RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any,
                        target: Target<Drawable?>,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.d("TAG", "onLoadFailed: $e")
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
                })
                .into(icon)
        }

        return mview
    }
}*/

class CustomSpinnerAdapterFuel(context: Context, items: ArrayList<AllCountryDataModl?>) :
    ArrayAdapter<AllCountryDataModl>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView =
                LayoutInflater.from(context).inflate(R.layout.custom_spinner_layout, parent, false)
        }

        val textViewItem = convertView!!.findViewById<TextView>(R.id.customSpinnerTV)
        textViewItem.text = getItem(position)?.name.toString()


        return convertView
    }
}