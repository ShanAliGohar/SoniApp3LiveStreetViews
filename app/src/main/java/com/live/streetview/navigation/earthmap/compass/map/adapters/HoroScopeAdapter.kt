package com.live.streetview.navigation.earthmap.compass.map.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.live.streetview.navigation.earthmap.compass.map.Model.horoscope.HoroScopeMainModel
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.callback.DialyHoroScopeCallBack
import com.live.streetview.navigation.earthmap.compass.map.callback.NearByActivityCallBack


class HoroScopeAdapter(
    var context: Context,
    var list: ArrayList<HoroScopeMainModel>
) :
    RecyclerView.Adapter<HoroScopeAdapter.ViewHolder>() {

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var iamgePerview: ImageView = itemView.findViewById(R.id.iamgePerview)
        var progressBar: ProgressBar = itemView.findViewById(R.id.progressbar)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.previewitem, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        try {
            Glide.with(context.applicationContext).load(list.get(position).Image)
                .addListener(object : RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any,
                        target: Target<Drawable?>,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.d("MYTAG", "onLoadFailed: " + e!!.localizedMessage)
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any,
                        target: Target<Drawable?>,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        holder.progressBar.visibility = View.GONE
                        return false
                    }
                }).into(holder.iamgePerview)
        } catch (e: GlideException) {
        }
//        recycler.onClick(list.get(position).name,list.get(position).Image)

    }

    override fun getItemCount(): Int {
        return list.size
    }
}