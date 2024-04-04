package com.live.streetview.navigation.earthmap.compass.map.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.callback.onClickItem
import com.live.streetview.navigation.earthmap.compass.map.database.Favourites


class FavSearchPlaceAdapter(
    var list: ArrayList<Favourites>,
    var context: Activity,
    val param: onClickItem,
) : RecyclerView.Adapter<FavSearchPlaceAdapter.ViewHolder>() {

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var txtName: TextView = itemView.findViewById(R.id.textView2)
        var txtCountry: TextView = itemView.findViewById(R.id.textView83)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtName.text = list[position].place
        holder.txtCountry.text = list[position].country
        holder.itemView.setOnClickListener {
            param.click(position)
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }
}

