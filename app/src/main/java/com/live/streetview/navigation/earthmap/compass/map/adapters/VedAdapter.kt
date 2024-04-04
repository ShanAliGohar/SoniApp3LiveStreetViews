package com.live.streetview.navigation.earthmap.compass.map.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.live.streetview.navigation.earthmap.compass.map.Model.VedModel
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.activities.CountryVediosActivity
import com.live.streetview.navigation.earthmap.compass.map.activities.Viewactivity
import com.squareup.picasso.Picasso

class VedAdapter(var context: CountryVediosActivity, var list: ArrayList<VedModel>) :
    RecyclerView.Adapter<VedAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.ved_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val vedModel = list[position]
        Picasso.get().load(vedModel.imagelink).into(holder.imagelink)
        holder.txtcountryname.text = vedModel.textcountryname
        // holder.imageplayer.setImageResource(vedModel.getPlayerlink());
        holder.itemView.setOnClickListener {
            val intent = Intent(context, Viewactivity::class.java)
            intent.putExtra("imageplayer", vedModel.playerlink)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imagelink: ImageView
        var imageplayer: ImageView
        var txtcountryname: TextView

        init {
            imagelink = itemView.findViewById(R.id.imageved)
            imageplayer = itemView.findViewById(R.id.imageplayer)
            txtcountryname = itemView.findViewById(R.id.textcountryname)
        }
    }
}
