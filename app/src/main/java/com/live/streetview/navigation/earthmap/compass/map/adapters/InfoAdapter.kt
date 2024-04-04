package com.live.streetview.navigation.earthmap.compass.map.adapters

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniBillingHelper
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds.loadSmartToolsBannerForMainMediationMedium
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds.meidationForClickSimpleSmartToolsLocation
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.RetrofitInfoModel.AllCountryDataModl
import com.live.streetview.navigation.earthmap.compass.map.activities.CountryInfoActivity
import com.live.streetview.navigation.earthmap.compass.map.activities.FlagInformartionActivity
import com.squareup.picasso.Picasso

class InfoAdapter(
    var context: CountryInfoActivity,
    var list: ArrayList<AllCountryDataModl?>,
    var view: View
) : RecyclerView.Adapter<InfoAdapter.ItemViewHolder>() {
    var typePostIso = 1
    var typeAdsIso = 0
    var emptyIso = 2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
//        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        View inflate = layoutInflater.inflate(R.layout.info_row, null);
//        InfoAdapter.ItemViewHolder viewHolder = new InfoAdapter.ItemViewHolder(inflate);
//        return viewHolder;
        var newLayout: View? = null
        if (viewType == typePostIso) {
            newLayout =
                LayoutInflater.from(parent.context).inflate(R.layout.info_row, parent, false)
        } else if (viewType == typeAdsIso) {
//            newLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.street_view_native_admob_layout, parent, false);
//            StreetViewAppSoniMyAppNativeAds.Companion.loadSmartToolsNavAdmobNativeAdPriority(context, (FrameLayout) newLayout);
//            Log.d("4545454","=====typeAds====");
            newLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.street_view_my_bannar_medium, parent, false)
            streetViewBannerAdsSmall(context, newLayout as LinearLayout?)
            //            StreetViewAppSoniMyAppNativeAds.Companion.loadSmartToolsNavAdmobNativeAdPriority(context, (LinearLayout) newLayout);
            Log.d("4545454", "=====typeAds====")
        } else if (viewType == emptyIso) {
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
        if (position % 16 == 0) return
        val myPostPosition = position - (position / 16 + 1)
        val allCountryDataModl = list[myPostPosition]
        Picasso.get().load(allCountryDataModl?.flags?.png).into(holder.imageflgainfo)
        holder.itemView.setOnClickListener { view ->
            logAnalyticsForClicks("StreetViewCountryInfoOnClickAnyCountry", context)
            CountryInfoActivity.modelList = allCountryDataModl
            val intent = Intent(context, FlagInformartionActivity::class.java)
            //    Bundle bundle = new Bundle();
//                bundle.putSerializable("key1", (Serializable) allCountryDataModl);
//                intent.putExtras(bundle);
            meidationForClickSimpleSmartToolsLocation(
                context,
                StreetViewAppSoniMyAppAds.admobInterstitialAd,
                intent, view
            )
        }
    }

    override fun getItemCount(): Int {
        val itemCount = list.size
        return itemCount + (itemCount / 15 + 1)
    }

    fun filterList(filteredList: ArrayList<AllCountryDataModl?>) {
        list = filteredList
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            emptyIso
        } else {
            if (position % 16 == 0) {
                typeAdsIso
            } else {
                typePostIso
            }
        }
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageflgainfo: ImageView? = null

        init {
            imageflgainfo = itemView.findViewById(R.id.infocountryimage)
        }
    }
}
