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
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.vehicle.VehicleEntity

class VehiclesAdapter(
    private val listener: AdapterListener,
    private val listOfVehicles: List<VehicleEntity>
) : RecyclerView.Adapter<VehiclesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.vehicle_recycler_item, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val vehicle = listOfVehicles[position]

        holder.itemView.setOnClickListener {
            listener.onEditClicked(position)
        }

        holder.vehicleName.text = vehicle.vehicleName
        val decodedImage = decodeBase64Image(vehicle.vehicleImage)
        holder.vehicleImage.setImageBitmap(decodedImage)
    }

    override fun getItemCount(): Int {

        return listOfVehicles.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        private val editVehicle: ImageView = itemView.findViewById(R.id.ivEditVehicle)
        val vehicleName: TextView = itemView.findViewById(R.id.vehilceName)
        val vehicleImage: ImageView = itemView.findViewById(R.id.imgVehilce)


        init {
            // Handle click on ivEditVehicle
            editVehicle.setOnClickListener {
                showPopupMenu(adapterPosition)
            }
        }

        private fun showPopupMenu(position: Int) {
            val popupMenu = PopupMenu(itemView.context, editVehicle)
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


// VehicleAdapterListener.kt
interface AdapterListener {
    fun onEditClicked(position: Int)
    fun onDeleteClicked(position: Int)
    fun onCarDescriptionClicked(position: Int)
}
