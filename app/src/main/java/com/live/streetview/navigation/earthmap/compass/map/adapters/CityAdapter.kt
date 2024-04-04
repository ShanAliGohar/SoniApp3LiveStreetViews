package com.live.streetview.navigation.earthmap.compass.map.adapters

import android.app.Activity
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
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
import com.live.streetview.navigation.earthmap.compass.map.Model.CityModel
import com.live.streetview.navigation.earthmap.compass.map.R

//public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ItemViewHolder> {
//
//    Activity context;
//    ArrayList<CityModel> list;
//
//    public CityAdapter(Activity context, ArrayList<CityModel> list) {
//        this.context = context;
//        this.list = list;
//    }
//
//    @NonNull
//    @Override
//    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View newLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_row, parent, false);
//        return new ItemViewHolder(newLayout);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
////        int myPostPosition = position - (position / 3 + 1);
//        CityModel cityModel = list.get(position);
//        holder.citytext.setText(cityModel.getText());
//
//        Glide.with(context).load(cityModel.getPic()).addListener(new RequestListener<Drawable>() {
//            @Override
//            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                holder.progress.setVisibility(View.VISIBLE);
//                return false;
//            }
//
//            @Override
//            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                holder.progress.setVisibility(View.GONE);
//                return false;
//            }
//        }).into(holder.cityImage);
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Handle item click
//                Intent i = new Intent(context, PlacesActivity.class);
//                i.putExtra("Cityname", cityModel.getText());
//                i.putExtra("key", UtilityClass.countryName);
//                context.startActivity(i);
//            }
//        });
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return 0; // You only have one view type
//    }
//
//    @Override
//    public int getItemCount() {
//        return list.size();
//    }
//
//    public class ItemViewHolder extends RecyclerView.ViewHolder {
//        private ImageView cityImage;
//        private TextView citytext;
//        private ProgressBar progress;
//
//        public ItemViewHolder(@NonNull View itemView) {
//            super(itemView);
//            progress = itemView.findViewById(R.id.progressbar);
//            cityImage = itemView.findViewById(R.id.placeimage);
//            citytext = itemView.findViewById(R.id.placetxt);
//        }
//    }
//}
class CityAdapter(
    var context: Activity,
    var list: ArrayList<CityModel>,
    private val onItemClickListener: OnItemClickListener?
) : RecyclerView.Adapter<CityAdapter.ItemViewHolder>() {
    var typePostCity = 1
    var typeAdsCity = 0
    var emptyCity = 2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

/*          val inflater=LayoutInflater.from(parent.context)
        val view=inflater.inflate(R.layout.city_row,parent,false)
        return ItemViewHolder(view)*/
        var newLayout: View? = null
        if (viewType == typePostCity) {
            newLayout =
                LayoutInflater.from(parent.context).inflate(R.layout.city_row, parent, false)
        } else if (viewType == typeAdsCity) {
            newLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.street_view_my_bannar_medium, parent, false)
            streetViewBannerAdsSmall(context, newLayout as LinearLayout?)
            //            StreetViewAppSoniMyAppNativeAds.Companion.loadSmartToolsNavAdmobNativeAdPriority(context, (FrameLayout) newLayout);
            Log.d("4545454", "=====typeAds====")
        } else if (viewType == emptyCity) {
            //newLayout = LayoutInflater.from(parent.context).inflate(R.layout.empty, parent, false)

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
        if (position % 5 == 0) return
        val myPostPosition = position - (position / 5 + 1)
        val cityModel = list[myPostPosition]
        holder.citytext?.text = cityModel.text
        //  Picasso.get().load(cityModel.getPic()).into(holder.cityImage);
//        Glide.with(context).load(cityModel.getPic()).into(holder.cityImage);
        Glide.with(context).load(cityModel.pic).addListener(object : RequestListener<Drawable?> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any,
                target: Target<Drawable?>,
                isFirstResource: Boolean
            ): Boolean {
                holder.progress?.visibility = View.VISIBLE
                return false
            }
            override fun onResourceReady(
                resource: Drawable?,
                model: Any,
                target: Target<Drawable?>,
                dataSource: DataSource,
                isFirstResource: Boolean
            ): Boolean {
                holder.progress?.visibility = View.GONE
                return false
            }
        }).into(holder.cityImage!!)
        holder.itemView.setOnClickListener { view: View? ->
            onItemClickListener?.onItemClick(
                cityModel
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            emptyCity
        } else {
            if (position % 5 == 0) {
                typeAdsCity
            } else {
                typePostCity
            }
        }
    }
    override fun getItemCount(): Int {
//         return list.size();
        val itemCount = list.size
        return itemCount + (itemCount / 4 + 1)
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cityImage: ImageView?= null
        var citytext: TextView? = null
        var progress: ProgressBar? = null

        init {
            progress = itemView.findViewById(R.id.progressbar)
            cityImage = itemView.findViewById(R.id.placeimage)
            citytext = itemView.findViewById(R.id.placetxt)
        }
    }
}

