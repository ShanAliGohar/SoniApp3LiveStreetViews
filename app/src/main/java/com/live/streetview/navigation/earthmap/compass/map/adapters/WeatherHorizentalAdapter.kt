package com.live.streetview.navigation.earthmap.compass.map.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.live.streetview.navigation.earthmap.compass.map.Model.weatherModel.CustomWeatherModel
import com.live.streetview.navigation.earthmap.compass.map.R
import java.text.SimpleDateFormat
import java.util.Date

class WeatherHorizentalAdapter(var context: Context, var list: ArrayList<CustomWeatherModel>) :
    RecyclerView.Adapter<WeatherHorizentalAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view =
            LayoutInflater.from(context).inflate(R.layout.weatherhorizental_row, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val weatherHorizentalModel = list[position]
        holder.textdailytime.text = getTimeFromTimeStamp(weatherHorizentalModel.date)
        //       holder.imagedaily.setImageResource(weatherHorizentalModel.getPic());
        // holder.textdailydegree.setText(toCelciusLiveEarth(weatherHorizentalModel.getTemp()));
        holder.textdailydegree.text =
            toCelciusLiveEarth(weatherHorizentalModel.temp).toString() + " ℃"
    }

    fun toCelciusLiveEarth(x: Double): Int {
        return (x - 273.0).toInt()
    }

    fun getTimeFromTimeStamp(timestamp: Int): String {
        val dv = timestamp * 1000L
        val df = Date(dv)
        return SimpleDateFormat("hh:mma").format(df)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imagedaily: ImageView
        var textdailytime: TextView
        var textdailydegree: TextView

        init {
            imagedaily = itemView.findViewById(R.id.weekimage)
            textdailytime = itemView.findViewById(R.id.texttdailytime)
            textdailydegree = itemView.findViewById(R.id.textdaillydegree)
        }
    }
}
