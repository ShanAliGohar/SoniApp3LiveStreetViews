package com.live.streetview.navigation.earthmap.compass.map.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.live.streetview.navigation.earthmap.compass.map.Model.ToolModel
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.activities.callack.toolsCallback

class ToolAdapter(
    var context: Context,
    var list: ArrayList<ToolModel>,
    var radioClickCallBack: toolsCallback,
    var view: View
) : RecyclerView.Adapter<ToolAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.tool_row, parent, false)
        return ItemViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(
        holder: ItemViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val toolModel = list[position]
        holder.txttool?.text = toolModel.texttool
        holder.imagetool?.setImageResource(toolModel.pic)
        when (position) {
            0 -> holder.constraintLayout?.setCardBackgroundColor(Color.parseColor("#FF8E8E"))
            1 -> holder.constraintLayout?.setCardBackgroundColor(Color.parseColor("#FFC72D"))
            2 -> holder.constraintLayout?.setCardBackgroundColor(Color.parseColor("#E25151"))
            3 -> holder.constraintLayout?.setCardBackgroundColor(Color.parseColor("#20746E"))
            4 -> holder.constraintLayout?.setCardBackgroundColor(Color.parseColor("#59B4A7"))
            5 -> holder.constraintLayout?.setCardBackgroundColor(Color.parseColor("#6495ED"))
            6 -> holder.constraintLayout?.setCardBackgroundColor(Color.parseColor("#FFC72D"))
            7 -> holder.constraintLayout?.setCardBackgroundColor(Color.parseColor("#20746E"))
        }
        holder.itemView.setOnClickListener {
            radioClickCallBack.onItemClick(position)
            /*switch (position) {
                        case 0: {
    //                        StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                            StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks("StreetViewMainScreenOnClickEarthMap2D", context);
    //                        StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                            Intent intent = new Intent(context, EarthMap2DActivity.class);
                            StreetViewAppSoniMyAppShowAds.meidationForClickSimpleSmartToolsLocation(context,
                                    StreetViewAppSoniMyAppAds.admobInterstitialAd,  intent,view);
                            break;
                        }
                        case 1: {
    //                        StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                            StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks("StreetViewMainScreenOnWebcamActivity", context);
                            Intent intent = new Intent(context, WebcamActivity.class);
                            StreetViewAppSoniMyAppShowAds.meidationForClickSimpleSmartToolsLocation(context,
                                    StreetViewAppSoniMyAppAds.admobInterstitialAd,  intent,view);
                            break;
                        }
    
    
                        case 2: {
    //                        StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                            radioClickCallBack.callBackForPermissionChecking("areaCalculator");
                            break;
                        }
                        case 3: {
    //                        StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                            StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks("StreetViewMainScreenOnClickFuelCalculatorActivity", context);
                            Intent intent = new Intent(context, FuelCalculatorActivity.class);
                            StreetViewAppSoniMyAppShowAds.meidationForClickSimpleSmartToolsLocation(context,
                                    StreetViewAppSoniMyAppAds.admobInterstitialAd,  intent,view);
    //                        radioClickCallBack.callBackForPermissionChecking("distancaCall");
                            break;
                        }
    
                        case 4:
    
                        {
    //                        StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                            StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks("StreetViewMainScreenOnClickFMActivity", context);
                            Intent intent2 = new Intent(context, FMActivity.class);
                            StreetViewAppSoniMyAppShowAds.meidationForClickSimpleSmartToolsLocation(context,
                                    StreetViewAppSoniMyAppAds.admobInterstitialAd,  intent2,view);
                            break;
                        }
                        case 5:
    
                        {
    //                        StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                            StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks("StreetViewMainScreenOnClickCountryInfoActivity", context);
                            Intent intent3 = new Intent(context, CountryInfoActivity.class);
                            StreetViewAppSoniMyAppShowAds.meidationForClickSimpleSmartToolsLocation(context,
                                    StreetViewAppSoniMyAppAds.admobInterstitialAd,  intent3,view);
                            break;
                        }
    
                        case 6: {
    //                        StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                            StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks("StreetViewMainScreenOnClickAltimeter", context);
                            Intent intent3 = new Intent(context, AltimeterActivity.class);
                            StreetViewAppSoniMyAppShowAds.meidationForClickSimpleSmartToolsLocation(context,
                                    StreetViewAppSoniMyAppAds.admobInterstitialAd,  intent3,view);
    //                        radioClickCallBack.callBackForPermissionChecking("compass");
                            break;
                        }
                        case 7: {
    */
            /*
                            radioClickCallBack.callBackForPermissionChecking("compass");
    */
            /*
    //                        StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                            StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks("StreetViewMainScreenOnClickNewCompassActivity", context);
                            Intent intent4 = new Intent(context, NewCompassActivity.class);
                            StreetViewAppSoniMyAppShowAds.meidationForClickSimpleSmartToolsLocation(context,
                                    StreetViewAppSoniMyAppAds.admobInterstitialAd,
                                     intent4,view);
                            break;
                        }
                        case 8: {
    //                        StreetViewAppSoniMyAppShowAds.setFirstShow(false);
                            StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks("StreetViewMainScreenOnClickSpeedMeterActivity", context);
                            Intent intent5 = new Intent(context, SpeedMeterActivity.class);
                            StreetViewAppSoniMyAppShowAds.meidationForClickSimpleSmartToolsLocation(context,
                                    StreetViewAppSoniMyAppAds.admobInterstitialAd,  intent5,view);
                            break;
                        }
    
    //                    case 9: {
    ////                        StreetViewAppSoniMyAppShowAds.setFirstShow(false);
    //                        Intent intent5 = new Intent(context, ISDCodeActivity.class);
    //                        StreetViewAppSoniMyAppShowAds.meidationForClickSimpleSmartToolsLocation(context,
    //                                StreetViewAppSoniMyAppAds.admobInterstitialAd,  intent5);
    //                        break;
    //                    }
    
                        default:
                            //   throw new IllegalStateException("Unexpected value: " + position);
                    }*/
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var constraintLayout: CardView? = null
        var txttool: TextView? = null
        var imagetool: ImageView? = null

        init {
            constraintLayout = itemView.findViewById(R.id.constraint)
            txttool = itemView.findViewById(R.id.textViewtool)
            imagetool = itemView.findViewById(R.id.imagetool)
        }
    }
}
