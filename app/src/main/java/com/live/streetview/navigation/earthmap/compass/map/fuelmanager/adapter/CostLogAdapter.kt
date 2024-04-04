package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.costs.CostEntity
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.vehicle.VehicleEntity
import kotlin.math.cos

class CostLogAdapter(
    private val listener: AdapterListener,
    private val costList: List<CostEntity>
) : RecyclerView.Adapter<CostLogAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cost_log_recycler, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val cost = costList[position]

        holder.costLogTitle.text = cost.costTitle
        holder.costLogDate.text = cost.selectDate
        holder.costLogAmount.text = cost.totalCost.toString()
        holder.costLogRemainder.text = cost.reminderDate
        holder.costLogOdometer.text= cost.reminderOdometer.toString()
        holder.costLogDateTime.text = cost.selectDate + " , " + cost.selectTime
        holder.costLogRepeat.text = cost.reminderRepeatDistance.toString() + "KM , " + cost.reminderRepeatMonths + "Months"


        holder.itemView.setOnClickListener {
            listener.onEditClicked(position)
        }

    }

    override fun getItemCount(): Int {

        return costList.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val ivEditCost: ImageView = itemView.findViewById(R.id.ivEditCost)
        val costLogTitle: TextView = itemView.findViewById(R.id.costLogTitle)
        val costLogDate: TextView = itemView.findViewById(R.id.costLogDate)
        val costLogAmount: TextView = itemView.findViewById(R.id.costLogAmount)
        val costLogRemainder: TextView = itemView.findViewById(R.id.costLogRemainder)
        val costLogOdometer: TextView = itemView.findViewById(R.id.costLogOdometer)
        val costLogDateTime: TextView = itemView.findViewById(R.id.costLogDateTime)
        val costLogRepeat: TextView = itemView.findViewById(R.id.costLogRepeatEvery)


        init {
            // Handle click on ivEditVehicle
            ivEditCost.setOnClickListener {
                showPopupMenu(adapterPosition)
            }
        }

        private fun showPopupMenu(position: Int) {
            val popupMenu = PopupMenu(itemView.context, ivEditCost)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_edit -> {
                        listener.onEditClicked(position)
                        true
                    }

                    R.id.menu_delete -> {
                        listener.onDeleteClicked(position)
                        true
                    }

                    R.id.car_description -> {
                        listener.onCarDescriptionClicked(position)
                        true
                    }

                    else -> false
                }
            }

            popupMenu.show()
        }
    }


    private fun decodeBase64Image(base64Image: String?): Bitmap? {
        base64Image?.let {
            val decodedBytes = Base64.decode(it, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        }
        return null
    }

}


