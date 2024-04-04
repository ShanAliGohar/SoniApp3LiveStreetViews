package com.live.streetview.navigation.earthmap.compass.map.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks
import com.live.streetview.navigation.earthmap.compass.map.Model.WebcamModel
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.activities.WebcamActivity
import com.live.streetview.navigation.earthmap.compass.map.activities.YotubeActivity

class WebcamAdapter(var context: WebcamActivity, var list: ArrayList<WebcamModel>) :
    RecyclerView.Adapter<WebcamAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        /* View view = LayoutInflater.from(context).inflate(R.layout.web_row, parent, false);
       return new ItemViewHolder(view);*/
        val view = LayoutInflater.from(context).inflate(R.layout.web_row, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val wecamModel = list[position]
        holder.txtCountryName?.text = wecamModel.textcountryname
        Glide.with(context).load("http://img.youtube.com/vi/" + wecamModel.videoLink + "/0.jpg")
            .placeholder(R.drawable.ic_launcher_background).into(holder.imageView)
        holder.cardView.setOnClickListener {
            logAnalyticsForClicks(
                "StreetViewWebcamActivityClick" + wecamModel.textcountryname,
                context
            )
            val intent = Intent(context, YotubeActivity::class.java)
            intent.putExtra("vedlink", wecamModel.videoLink)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView
        val txtCountryName: TextView
        val imageView: ImageView
        private val imageplay: ImageView? = null

        init {
            cardView = itemView.findViewById(R.id.card)
            txtCountryName = itemView.findViewById(R.id.webcamcountryname)
            imageView = itemView.findViewById(R.id.webcameimage)
            // imageplay=itemView.findViewById(R.id.imageplay);
        }
    }
}
