package com.live.streetview.navigation.earthmap.compass.map.adapters

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniBillingHelper
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds.loadSmartToolsBannerForMainMediationMedium
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds.meidationForClickSimpleSmartToolsLocation
import com.live.streetview.navigation.earthmap.compass.map.Model.PlaceModel
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.activities.ImageActivity
import com.live.streetview.navigation.earthmap.compass.map.activities.utils.UtilityClass

class PlacesAdapter(var context: Activity, list: ArrayList<PlaceModel?>, view: View) :
    RecyclerView.Adapter<PlacesAdapter.ItemViewHolder>() {
    var list = ArrayList<PlaceModel?>()
    var view: View
    var typePostPlace = 1
    var typeAdsPlace = 0
    var emptyPlace = 2
    init {
        this.list = list
        this.view = view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
/*         val inflater=LayoutInflater.from(parent.getContext());
        val view=inflater.inflate(R.layout.place_row,parent,false);
        return ItemViewHolder(view);*/
        var newLayout: View? = null
        if (viewType == typePostPlace) {
            newLayout =
                LayoutInflater.from(parent.context).inflate(R.layout.place_row, parent, false)
        } else if (viewType == typeAdsPlace) {
            newLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.street_view_my_bannar_medium, parent, false)
            streetViewBannerAdsSmall(context, newLayout as LinearLayout?)
            // StreetViewAppSoniMyAppNativeAds.Companion.loadSmartToolsNavAdmobNativeAdPriority(context, (FrameLayout) newLayout);
            Log.d("4545454", "=====typeAds====")
        } else if (viewType == emptyPlace) {
            newLayout = LayoutInflater.from(parent.context).inflate(R.layout.empty, parent, false)
        }
        Log.d("4545454", "=====view====$viewType")
        return ItemViewHolder(newLayout!!)
    }

    private fun streetViewBannerAdsSmall(context: Activity, newLayout: LinearLayout?) {
        val billingHelper = StreetViewAppSoniBillingHelper(context)
        val adView = AdView(context)
        adView.setAdSize(AdSize.MEDIUM_RECTANGLE)
        adView.adUnitId = StreetViewAppSoniMyAppAds.banner_admob_inApp_medium
        if (billingHelper.isNotAdPurchased) {
            loadSmartToolsBannerForMainMediationMedium(
                newLayout!!.findViewById(R.id.adContainer),
                adView,
                context
            )
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        if (position == 0) {
            return
        }

        if (position % 3 == 0) return
        val myPostPosition = position - (position / 3 + 1)
        val placeModel = list[myPostPosition]
        holder.placetext?.text = placeModel?.txtplacename
        // Glide.with(context).load(placeModel.getImageplace()).into(holder.imageplace);
        //Picasso.get().load(placeModel.getImageplace()).into(holder.imageplace);
        Glide.with(context).load(placeModel?.imageplace)
            .addListener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Drawable?>,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.progressBar1?.visibility = View.VISIBLE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any,
                    target: Target<Drawable?>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.progressBar1?.visibility = View.GONE
                    return false
                }
            }).into(holder.imageplace!!)
        holder.cardClick?.setOnClickListener { view ->
            logAnalyticsForClicks(
                "StreetViewDisplayStreetViewCountry" + placeModel?.txtplacename + "onClick",
                context
            )
            //                StreetViewAppSoniMyAppShowAds.setFirstShow(false);
            val intent = Intent(context, ImageActivity::class.java)
            intent.putExtra("showimage", placeModel?.imageplace)
            intent.putExtra("placeName", placeModel?.txtplacename)
            intent.putExtra("Cityname", UtilityClass.Cityname)
            intent.putExtra("key", UtilityClass.countryName)
            meidationForClickSimpleSmartToolsLocation(
                context,
                StreetViewAppSoniMyAppAds.admobInterstitialAd, intent, view
            )
            /* context.startActivity(intent);*/
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            emptyPlace
        } else {
            if (position % 3 == 0) {
                typeAdsPlace
            } else {
                typePostPlace
            }
        }
    }

    override fun getItemCount(): Int {
//        return list.size();
        val itemCount = list.size
        return itemCount + (itemCount / 3 + 1)
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageplace: ImageView? = null
        var placetext: TextView? = null
        var progressBar1: ProgressBar? = null
        var cardClick: ConstraintLayout? = null

        init {
            cardClick = itemView.findViewById(R.id.cardClick)
            progressBar1 = itemView.findViewById(R.id.progress1)
            imageplace = itemView.findViewById(R.id.placeimage)
            placetext = itemView.findViewById(R.id.placetxt)
        }
    }
}
