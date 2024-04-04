package com.live.streetview.navigation.earthmap.compass.map.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.live.streetview.navigation.earthmap.compass.map.Model.MessageModel
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.database.ChatRecord


class MessageAdapter(val context: Context, val list: ArrayList<ChatRecord>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_ONE = 1
        const val VIEW_TYPE_TWO = 2
    }


    private inner class GfgViewOne(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var gfgText: TextView = itemView.findViewById(R.id.txtsend)
        fun bind(position: Int) {
            val recyclerViewModel = list[position]
            gfgText.text = recyclerViewModel.message
        }
    }

    private inner class View2ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var gfgText: TextView = itemView.findViewById(R.id.txtRecieve)
        fun bind(position: Int) {
            val recyclerViewModel = list[position]
            gfgText.text = recyclerViewModel.message
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_ONE) {
            return GfgViewOne(
                LayoutInflater.from(context).inflate(R.layout.message_sending, parent, false)
            )
        }
        return View2ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.message_receiving, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (list[position].type === VIEW_TYPE_ONE) {
            (holder as GfgViewOne).bind(position)
        } else {
            (holder as View2ViewHolder).bind(position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].type
    }
}
