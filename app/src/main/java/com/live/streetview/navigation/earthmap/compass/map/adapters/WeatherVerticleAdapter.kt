package com.live.streetview.navigation.earthmap.compass.map.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.live.streetview.navigation.earthmap.compass.map.Model.weatherModel.CustomWeatherModel
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.activities.WeatherActivity
import java.text.SimpleDateFormat
import java.util.Date

class WeatherVerticleAdapter(
    var context: WeatherActivity,
    var list: ArrayList<CustomWeatherModel>
) : RecyclerView.Adapter<WeatherVerticleAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view =
            LayoutInflater.from(context).inflate(R.layout.weatherveerticle_row, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val customWeatherModel = list[position]
        holder.txtdate?.text = getTimeFromTimeStamp(customWeatherModel.date)
        holder.txtweekdegree?.text = toCelciusLiveEarth(customWeatherModel.temp).toString() + " ℃"
    }

    fun getTimeFromTimeStamp(timestamp: Int): String {
        val dv = timestamp * 1000L
        val df = Date(dv)
        return SimpleDateFormat("yyyy-MM-dd").format(df)
        //        return new SimpleDateFormat("yyyy-mm-dd hh:mma").format(df);
    }

    fun toCelciusLiveEarth(x: Double): Int {
        return (x - 273.0).toInt()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         var txtdate: TextView? = null
         var txtweekdegree: TextView? = null
         var imagenxtweek: ImageView? = null

        init {
            txtdate = itemView.findViewById(R.id.date)
            txtweekdegree = itemView.findViewById(R.id.nxtweekdegreee)
            imagenxtweek = itemView.findViewById(R.id.nextweekimage)
        }
    }
}
