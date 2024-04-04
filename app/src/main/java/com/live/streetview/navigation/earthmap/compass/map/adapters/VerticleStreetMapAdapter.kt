package com.live.streetview.navigation.earthmap.compass.map.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniBillingHelper
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds.loadSmartToolsBannerForMainMediationMedium
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds.meidationForClickSimpleSmartToolsLocation
import com.live.streetview.navigation.earthmap.compass.map.Model.CountryNameModel
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.activities.CityActivities

class VerticleStreetMapAdapter(
    var context: Activity,
    var list: ArrayList<CountryNameModel>,
    var view: View
) : RecyclerView.Adapter<VerticleStreetMapAdapter.ItemViewHolder>() {
    var typePost = 1
    var typeAds = 0
    var empty = 2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
/*         val inflater = LayoutInflater.from(parent.getContext());
        val view = inflater.inflate(R.layout.streetmapverticle_row, parent, false);
        return  ItemViewHolder(view);*/
        var newLayout: View? = null
        if (viewType == typePost) {
            newLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.streetmapverticle_row, parent, false)
            Log.d("TAG", "onCreateViewHolder:typePosttype post called ")
        } else if (viewType == typeAds) {
            newLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.street_view_my_bannar_medium, parent, false)
            streetViewBannerAdsSmall(context, newLayout as LinearLayout?)
            // StreetViewAppSoniMyAppNativeAds.Companion.loadSmartToolsNavAdmobNativeAdPriority(context, (FrameLayout) newLayout);
            Log.d("TAG", "onCreateViewHolder:typeAds  called ")

            Log.d("4545454", "=====typeAds====")
        } else if (viewType == empty) {
            Log.d("TAG", "onCreateViewHolder:empty called ")

            //newLayout = LayoutInflater.from(parent.context).inflate(R.layout.empty, parent, false)
            newLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.empty, parent, false)

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
       // if (position == 0) return
        if (position % 3 == 0) return
        val myPostPosition = position - (position / 3 + 1)
        val countryNameModel = list[myPostPosition]
        holder.txtcountryname?.text = countryNameModel.textcountryname
        settingFlags(holder, countryNameModel.textcountryname)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun settingFlags(holder: ItemViewHolder, textcountryname: String) {
        when (textcountryname) {
            "United States" -> {
                holder.countryimage?.setImageDrawable(context.resources.getDrawable(R.drawable.usaflag))
                holder.itemView.setOnClickListener { view ->
                    logAnalyticsForClicks(
                        "StreetViewDisplayStreetViewCountryOnClickUnitedStates",
                        context
                    )
                    //                        StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                    val intent = Intent(context, CityActivities::class.java)
                    intent.putExtra("key", "United States")
                    meidationForClickSimpleSmartToolsLocation(
                        context,
                        StreetViewAppSoniMyAppAds.admobInterstitialAd, intent, view
                    )
                }
            }

            "United Kingdom" -> {
                holder.countryimage?.setImageDrawable(context.resources.getDrawable(R.drawable.unitedkingdomflag))
                holder.itemView.setOnClickListener { view ->
                    logAnalyticsForClicks(
                        "StreetViewDisplayStreetViewCountryOnClickUnitedKingdom",
                        context
                    )
                    //                        StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                    val intent0 = Intent(context, CityActivities::class.java)
                    intent0.putExtra("key", "United Kingdom")
                    meidationForClickSimpleSmartToolsLocation(
                        context,
                        StreetViewAppSoniMyAppAds.admobInterstitialAd, intent0, view
                    )
                }
            }

            "Switzerland" -> {
                holder.countryimage?.setImageDrawable(context.resources.getDrawable(R.drawable.swisterlandflag))
                holder.itemView.setOnClickListener { view ->
                    logAnalyticsForClicks(
                        "StreetViewDisplayStreetViewCountryOnClickSwitzerland",
                        context
                    )
                    //                        StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                    val intent0 = Intent(context, CityActivities::class.java)
                    intent0.putExtra("key", "Switzerland")
                    meidationForClickSimpleSmartToolsLocation(
                        context,
                        StreetViewAppSoniMyAppAds.admobInterstitialAd, intent0, view
                    )
                }
            }

            "Germany" -> {
                holder.countryimage?.setImageDrawable(context.resources.getDrawable(R.drawable.germany))
                holder.itemView.setOnClickListener { view ->
                    logAnalyticsForClicks(
                        "StreetViewDisplayStreetViewCountryOnClickGermany",
                        context
                    )
                    //                        StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                    val intent1 = Intent(context, CityActivities::class.java)
                    intent1.putExtra("key", "Germany")
                    meidationForClickSimpleSmartToolsLocation(
                        context,
                        StreetViewAppSoniMyAppAds.admobInterstitialAd, intent1, view
                    )
                }
            }

            "France" -> {
                holder.countryimage?.setImageDrawable(context.resources.getDrawable(R.drawable.franceflag))
                holder.itemView.setOnClickListener { view ->
                    logAnalyticsForClicks(
                        "StreetViewDisplayStreetViewCountryOnClickFrance",
                        context
                    )
                    //                        StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                    val intent2 = Intent(context, CityActivities::class.java)
                    intent2.putExtra("key", "France")
                    meidationForClickSimpleSmartToolsLocation(
                        context,
                        StreetViewAppSoniMyAppAds.admobInterstitialAd, intent2, view
                    )
                }
            }

            "Canada" -> {
                holder.countryimage?.setImageDrawable(context.resources.getDrawable(R.drawable.canadaflag))
                holder.itemView.setOnClickListener {
                    logAnalyticsForClicks(
                        "StreetViewDisplayStreetViewCountryOnClickCanada",
                        context
                    )
                    //                        StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                    val intent3 = Intent(context, CityActivities::class.java)
                    intent3.putExtra("key", "Canada")
                    context.startActivity(intent3)
                }
            }

            "Australia" -> {
                holder.countryimage?.setImageDrawable(context.resources.getDrawable(R.drawable.ausflag))
                holder.itemView.setOnClickListener {
                    logAnalyticsForClicks(
                        "StreetViewDisplayStreetViewCountryOnClickAustralia",
                        context
                    )
                    //                        StreetViewAppSoniMyAppShowAds.setFirstShow(false);

//                        holder.countryimage?.setImageDrawable(context.getResources().getDrawable(R.drawable.aus));
                    val intent4 = Intent(context, CityActivities::class.java)
                    intent4.putExtra("key", "Australia")
                    context.startActivity(intent4)
                }
            }

            "Turkey" -> {
                holder.countryimage?.setImageDrawable(context.resources.getDrawable(R.drawable.turkey))
                holder.itemView.setOnClickListener {
                    logAnalyticsForClicks(
                        "StreetViewDisplayStreetViewCountryOnClickTurkey",
                        context
                    )
                    //                        StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                    val intent6 = Intent(context, CityActivities::class.java)
                    intent6.putExtra("key", "Turkey")
                    context.startActivity(intent6)
                }
            }

            "Norway" -> {
                holder.countryimage?.setImageDrawable(context.resources.getDrawable(R.drawable.norwayflag))
                holder.itemView.setOnClickListener {
                    logAnalyticsForClicks(
                        "StreetViewDisplayStreetViewCountryOnClickNorway",
                        context
                    )
                    //                        StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                    val intent7 = Intent(context, CityActivities::class.java)
                    intent7.putExtra("key", "Norway")
                    context.startActivity(intent7)
                }
            }

            "India" -> {
                holder.countryimage?.setImageDrawable(context.resources.getDrawable(R.drawable.indianflag))
                holder.itemView.setOnClickListener {
                    logAnalyticsForClicks(
                        "StreetViewDisplayStreetViewCountryOnClickIndia",
                        context
                    )
                    //                        StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                    val intent8 = Intent(context, CityActivities::class.java)
                    intent8.putExtra("key", "India")
                    context.startActivity(intent8)
                }
            }
        }
    }

    override fun getItemCount(): Int {
//         return list.size();
        val itemCount = list.size
        return itemCount + (itemCount / 3 + 1)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            empty
        } else {
            if (position % 3 == 0) {
                typeAds
            } else {
                typePost
            }
        }
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtcountryname: TextView? = null
        var cardCountry: CardView? = null
        var countryimage: ImageView? = null

        init {
            cardCountry = itemView.findViewById(R.id.cardCountry)
            countryimage = itemView.findViewById(R.id.placeimage)
            txtcountryname = itemView.findViewById(R.id.placetxt)
        }
    }
}
