package com.gps.navigation.streetview.earthmap.satelliteview.compass.pro.CountryInfoScreen.Adapters

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.gps.navigation.streetview.earthmap.satelliteview.compass.pro.CountryInfoModelStreetView
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniBillingHelper
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds.loadSmartToolsBannerForMainMediationMedium
import com.live.streetview.navigation.earthmap.compass.map.CountryInfoScreen.Interfaces.OnItemClickListener
import com.live.streetview.navigation.earthmap.compass.map.R
import java.util.*

class CountryFetchingAdapterStreetView(
    var context: Context,
    val countryInfoModelStreetView: List<CountryInfoModelStreetView>,
    val onItemClickListener: OnItemClickListener,
) : RecyclerView.Adapter<CountryFetchingAdapterStreetView.ViewHolder>() {
    var typeAds = 0
    var typepost = 1
    val empty: Int = 2


    override fun onCreateViewHolder(
        p0: ViewGroup,
        p1: Int,
    ): ViewHolder {
        var newLayout: View? = null
        if (p1 == typepost) {
            newLayout =
                LayoutInflater.from(p0.context).inflate(R.layout.countryinfo_view, p0, false)
        } else if (p1 == typeAds) {
            newLayout = LayoutInflater.from(p0.context)
                .inflate(R.layout.street_view_my_bannar_medium, p0, false)
            streetViewBannerAdsSmall(context as Activity, (newLayout as LinearLayout?)!!)
        } else if (p1 == empty) {
            newLayout = LayoutInflater.from(p0.context).inflate(R.layout.empty, p0, false)
        }
        return ViewHolder(newLayout!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, p1: Int) {
        try {
            if (p1 == 0) {
                return
            }
            if (p1 % 7 == 0) return
            val myPositionNew = p1 - (p1 / 7 + 1)

            val modelStreetView: CountryInfoModelStreetView = countryInfoModelStreetView[myPositionNew]
            modelStreetView.currencies[0].name
            holder.countryName!!.text = modelStreetView.name
            val flagString = "https://flagpedia.net/data/flags/normal/" +
                    modelStreetView.alpha2Code.lowercase(Locale.US) +
                    ".png"
            try {
                Glide.with(context).load(flagString).listener(object :
                    RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any,
                        target: Target<Drawable?>,
                        isFirstResource: Boolean,
                    ): Boolean {
                        holder.progressBar!!.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any,
                        target: Target<Drawable?>,
                        dataSource: DataSource,
                        isFirstResource: Boolean,
                    ): Boolean {
                        holder.progressBar!!.visibility = View.GONE
                        return false
                    }
                }).into(holder.countryFlag!!)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            holder.itemView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    onItemClickListener.onItemClick(myPositionNew)
                }

            })
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return empty
        } else {
            if (position % 7 == 0) {
                return typeAds
            } else {
                return typepost
            }
        }
    }

    override fun getItemCount(): Int {
        // return item.size
        val itemCount = countryInfoModelStreetView.size
        return itemCount + ((itemCount / 6) + 1)
    }

    private fun streetViewBannerAdsSmall(context: Activity, newLayout: LinearLayout) {
        val billingHelper = StreetViewAppSoniBillingHelper(context)
        val adView = AdView(context)
        adView.setAdSize(AdSize.MEDIUM_RECTANGLE)
        adView.adUnitId = StreetViewAppSoniMyAppAds.banner_admob_inApp_medium
        if (billingHelper.isNotAdPurchased) {
            loadSmartToolsBannerForMainMediationMedium(
                newLayout.findViewById(R.id.adContainer),
                adView,
                context
            )
        }
    }
    inner class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        var countryFlag: ImageView? = null
        var countryName: TextView? = null
        var progressBar: ProgressBar? = null

        init {
            countryFlag = itemView.findViewById(R.id.countryFlag)
            countryName = itemView.findViewById(R.id.countryName)
            progressBar = itemView.findViewById(R.id.countryInfoProgress)
        }
    }
}