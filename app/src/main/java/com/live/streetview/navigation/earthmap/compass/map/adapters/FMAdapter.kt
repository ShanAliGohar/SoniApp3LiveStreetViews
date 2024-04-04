package com.live.streetview.navigation.earthmap.compass.map.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.activities.FMActivity
import com.live.streetview.navigation.earthmap.compass.map.activities.callack.RadioClickCallBack
import com.live.streetview.navigation.earthmap.compass.map.fmmodel.CountryFMModelInformation

class FMAdapter(
    var context: FMActivity,
    var list: ArrayList<CountryFMModelInformation?>,
    var callBack: RadioClickCallBack
) : RecyclerView.Adapter<FMAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = LayoutInflater.from(context).inflate(R.layout.fm_row, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ItemViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val countryFMModelInformation = list[position]
        holder.channelname?.text = countryFMModelInformation?.name
        holder.itemView.setOnClickListener {
            callBack.onClickListener(position)
            //callBack.onClickListener(12);
        }

        //  holder.channelid.setText(countryFMModelInformation.getChangeuuid());
        //  Picasso.get().load(countryFMModelInformation.getFavicon()).into(holder.image);
        Glide.with(context).load(countryFMModelInformation?.favicon).into(holder.image!!)

        //  Log.d(TAG, "onBindViewHolder: "+countryFMModelInformation.getFavicon());
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView? = null
        var channelname: TextView? = null
        val channelid: TextView? = null

        init {
            image = itemView.findViewById(R.id.bg_imagefm)
            channelname = itemView.findViewById(R.id.channelName)
            //  channelid =itemView.findViewById(R.id.channelid);
        }
    }
}
