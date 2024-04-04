package com.live.streetview.navigation.earthmap.compass.map.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.callback.DeleteItem
import com.live.streetview.navigation.earthmap.compass.map.callback.onClickItem
import com.live.streetview.navigation.earthmap.compass.map.database.ChatRecord

class ChatHistoryAdapter(
    var list: ArrayList<ChatRecord>,
    var context: Activity,
    val param: onClickItem,
   val param1: DeleteItem,
) : RecyclerView.Adapter<ChatHistoryAdapter.ViewHolder>() {

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var txtName: TextView = itemView.findViewById(R.id.textView2)
        var txtDate: TextView = itemView.findViewById(R.id.textView83)
        var delete: ImageView = itemView.findViewById(R.id.imageView53)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.chathistory, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.txtName.text = list[position].message
        holder.txtDate.text = list[position].date
        holder.itemView.setOnClickListener {
            param.click(position)

        }
        holder.delete.setOnClickListener {
            param1.delteItem(list[position].chat)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}