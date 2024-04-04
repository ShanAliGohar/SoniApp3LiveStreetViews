package com.live.streetview.navigation.earthmap.compass.map.adapters

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniBillingHelper
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds.loadSmartToolsBannerForMainMediationMedium
import com.live.streetview.navigation.earthmap.compass.map.Model.IsoModel
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.activities.ISDCodeActivity
import com.live.streetview.navigation.earthmap.compass.map.adapters.IsoAdapter.ItemViewHodler
import com.squareup.picasso.Picasso

class IsoAdapter(
    var context: Activity,
    var ISDCodeActivity: ISDCodeActivity,
    var list: ArrayList<IsoModel>?
) : RecyclerView.Adapter<ItemViewHodler>() {
    var typePostIso = 1
    var typeAdsIso = 0
    var emptyIso = 2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHodler {
/*         val view = LayoutInflater.from(ISDCodeActivity).inflate(R.layout.iso_rowlayout, parent, false);
        return  ItemViewHodler(view);*/
        var newLayout: View? = null
        if (viewType == typePostIso) {
            newLayout =
                LayoutInflater.from(parent.context).inflate(R.layout.iso_rowlayout, parent, false)
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
        return ItemViewHodler(newLayout!!)
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

    override fun onBindViewHolder(holder: ItemViewHodler, position: Int) {
        if (position == 0) {
            return
        }
        if (position % 6 == 0) return
        val myPostPosition = position - (position / 6 + 1)
        val isoModel = list?.get(myPostPosition)
        Picasso.get().load(isoModel?.flagUrl).into(holder.imageflag)
        holder.textViewcountrycode?.text = "+" + isoModel?.countryCodeNumber
        holder.textViewcountryname?.text = isoModel?.name
    }

    override fun getItemCount(): Int {
//        return list.size();
        val itemCount = list?.size
        if (itemCount != null) {
            return itemCount + (itemCount / 6 + 1)
        } else return 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            emptyIso
        } else {
            if (position % 6 == 0) {
                typeAdsIso
            } else {
                typePostIso
            }
        }
    }

    fun filterList(filteredList: ArrayList<IsoModel>?) {
        list = filteredList
        notifyDataSetChanged()
    }

    inner class ItemViewHodler(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageflag: ImageView? = null
        //var adLayout: LinearLayout
        var textViewcountryname: TextView? = null
        var textViewcountrycode: TextView? = null

        init {
            imageflag = itemView.findViewById(R.id.imageflagsiso)
            textViewcountryname = itemView.findViewById(R.id.countryname)
            textViewcountrycode = itemView.findViewById(R.id.codes)
          //  adLayout = itemView.findViewById(R.id.adContainer)
        }
    }
}
