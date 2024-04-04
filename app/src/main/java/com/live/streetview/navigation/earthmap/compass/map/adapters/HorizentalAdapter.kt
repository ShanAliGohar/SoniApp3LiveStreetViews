package com.live.streetview.navigation.earthmap.compass.map.adapters

import android.content.Context
import android.content.Intent
import android.content.res.Resources.NotFoundException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds.meidationForClickSimpleSmartToolsLocation
import com.live.streetview.navigation.earthmap.compass.map.Model.CountryNameModel
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.activities.CityActivities

class HorizentalAdapter(
    var context: Context,
    var list: ArrayList<CountryNameModel>,
    var view: View
) : RecyclerView.Adapter<HorizentalAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.horizental_row, parent, false)
        return ItemViewHolder(view)
        //goFlagsActivity();
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val streetViewModel = list[position]
        // holder.imageStreetview.setImageResource(streetViewModel.getPic());
        // goFlagsActivity(holder,streetViewModel.getPic());
        holder.txtName.text = streetViewModel.textcountryname
        settingFlags(holder, streetViewModel.textcountryname)
    }

    private fun settingFlags(holder: ItemViewHolder, textcountryname: String) {
        when (textcountryname) {
            "United States" -> {
                try {
                    Glide.with(context).load(context.resources.getDrawable(R.drawable.usa))
                        .into(holder.imageStreetview)
                } catch (e: NotFoundException) {
                    e.printStackTrace()
                }
                holder.itemView.setOnClickListener { view -> //                        StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                    val intent = Intent(context, CityActivities::class.java)
                    intent.putExtra("key", "United States")
                    meidationForClickSimpleSmartToolsLocation(
                        context, StreetViewAppSoniMyAppAds.admobInterstitialAd,
                        intent, view
                    )
                }
            }

            "United Kingdom" -> {
                try {
                    Glide.with(context).load(context.resources.getDrawable(R.drawable.uk))
                        .into(holder.imageStreetview)
                } catch (e: NotFoundException) {
                    e.printStackTrace()
                }
                holder.itemView.setOnClickListener { view -> //                        StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                    val intent0 = Intent(context, CityActivities::class.java)
                    intent0.putExtra("key", "United Kingdom")
                    meidationForClickSimpleSmartToolsLocation(
                        context, StreetViewAppSoniMyAppAds.admobInterstitialAd,
                        intent0, view
                    )
                }
            }

            "Switzerland" -> {
                try {
                    Glide.with(context).load(context.resources.getDrawable(R.drawable.swit))
                        .into(holder.imageStreetview)
                } catch (e: NotFoundException) {
                    e.printStackTrace()
                }
                holder.itemView.setOnClickListener { view -> //                        StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                    val intent0 = Intent(context, CityActivities::class.java)
                    intent0.putExtra("key", "Switzerland")
                    meidationForClickSimpleSmartToolsLocation(
                        context, StreetViewAppSoniMyAppAds.admobInterstitialAd,
                        intent0, view
                    )
                }
            }

            "Germany" -> {
                try {
                    Glide.with(context).load(context.resources.getDrawable(R.drawable.ger))
                        .into(holder.imageStreetview)
                } catch (e: NotFoundException) {
                    e.printStackTrace()
                }
                holder.itemView.setOnClickListener { view -> //                        StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                    val intent1 = Intent(context, CityActivities::class.java)
                    intent1.putExtra("key", "Germany")
                    meidationForClickSimpleSmartToolsLocation(
                        context, StreetViewAppSoniMyAppAds.admobInterstitialAd,
                        intent1, view
                    )
                }
            }

            "France" -> {
                try {
                    Glide.with(context).load(context.resources.getDrawable(R.drawable.franceee))
                        .into(holder.imageStreetview)
                } catch (e: NotFoundException) {
                    e.printStackTrace()
                }
                holder.itemView.setOnClickListener { view -> //                        StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                    val intent2 = Intent(context, CityActivities::class.java)
                    intent2.putExtra("key", "France")
                    meidationForClickSimpleSmartToolsLocation(
                        context, StreetViewAppSoniMyAppAds.admobInterstitialAd,
                        intent2, view
                    )
                }
            }

            "Canada" -> {
                try {
                    Glide.with(context).load(context.resources.getDrawable(R.drawable.canada))
                        .into(holder.imageStreetview)
                } catch (e: NotFoundException) {
                    e.printStackTrace()
                }
                holder.itemView.setOnClickListener { view -> //                        StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                    val intent3 = Intent(context, CityActivities::class.java)
                    intent3.putExtra("key", "Canada")
                    meidationForClickSimpleSmartToolsLocation(
                        context, StreetViewAppSoniMyAppAds.admobInterstitialAd,
                        intent3, view
                    )
                }
            }

            "Australia" -> {
                holder.imageStreetview.setImageDrawable(context.resources.getDrawable(R.drawable.aus))
                try {
                    Glide.with(context).load(context.resources.getDrawable(R.drawable.aus))
                        .into(holder.imageStreetview)
                } catch (e: NotFoundException) {
                    e.printStackTrace()
                }
                holder.itemView.setOnClickListener { view ->
                    try {
                        Glide.with(context).load(context.resources.getDrawable(R.drawable.aus))
                            .into(holder.imageStreetview)
                    } catch (e: NotFoundException) {
                        e.printStackTrace()
                    }
                    //                        StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                    val intent4 = Intent(context, CityActivities::class.java)
                    intent4.putExtra("key", "Australia")
                    meidationForClickSimpleSmartToolsLocation(
                        context, StreetViewAppSoniMyAppAds.admobInterstitialAd,
                        intent4, view
                    )
                }
            }

            "Turkey" -> {
                try {
                    Glide.with(context).load(context.resources.getDrawable(R.drawable.turkey))
                        .into(holder.imageStreetview)
                } catch (e: NotFoundException) {
                    e.printStackTrace()
                }
                holder.itemView.setOnClickListener { view -> //                        StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                    val intent6 = Intent(context, CityActivities::class.java)
                    intent6.putExtra("key", "Turkey")
                    meidationForClickSimpleSmartToolsLocation(
                        context, StreetViewAppSoniMyAppAds.admobInterstitialAd,
                        intent6, view
                    )
                }
            }

            "Norway" -> {
                try {
                    Glide.with(context).load(context.resources.getDrawable(R.drawable.norway))
                        .into(holder.imageStreetview)
                } catch (e: NotFoundException) {
                    e.printStackTrace()
                }
                holder.itemView.setOnClickListener { view -> //                        StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                    val intent7 = Intent(context, CityActivities::class.java)
                    intent7.putExtra("key", "Norway")
                    meidationForClickSimpleSmartToolsLocation(
                        context, StreetViewAppSoniMyAppAds.admobInterstitialAd,
                        intent7, view
                    )
                }
            }

            "India" -> {
                try {
                    Glide.with(context).load(context.resources.getDrawable(R.drawable.india))
                        .into(holder.imageStreetview)
                } catch (e: NotFoundException) {
                    e.printStackTrace()
                }
                holder.itemView.setOnClickListener { view -> //                        StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                    val intent8 = Intent(context, CityActivities::class.java)
                    intent8.putExtra("key", "India")
                    meidationForClickSimpleSmartToolsLocation(
                        context, StreetViewAppSoniMyAppAds.admobInterstitialAd,
                        intent8, view
                    )
                }
            }
        }
    }

    /* private void goFlagsActivity(ItemViewHolder holder,int pic){
        switch (pic){
            case
        }

    }*/
    override fun getItemCount(): Int {
        return list.size
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardViewHorixental: CardView
        var imageStreetview: ImageView
        var txtName: TextView

        init {
            cardViewHorixental = itemView.findViewById(R.id.cardViewHorizental)
            imageStreetview = itemView.findViewById(R.id.imageflag)
            txtName = itemView.findViewById(R.id.textView26)
        }
    }
}
