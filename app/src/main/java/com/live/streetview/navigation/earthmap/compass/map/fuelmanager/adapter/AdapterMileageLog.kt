package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.refueling.MileageLogEntity
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.vehicle.VehicleDao
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.utils.SharedPrefManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdapterMileageLog(
    private val array: List<MileageLogEntity>,
    private val vehicleDao: VehicleDao,
    private val context: Context
) : RecyclerView.Adapter<AdapterMileageLog.TripViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_mileage_log, parent, false)
        return TripViewHolder(view)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {

        val milageLog = array[position]

        holder.pumpNameTextView.text = milageLog.gasStationType
        holder.dateTextView.text = milageLog.date
        holder.priceTextView.text = SharedPrefManager.getInstance(context).getDefaultCurrency() + " " + milageLog.price.toString()
        holder.discountTextView.text = SharedPrefManager.getInstance(context).getDefaultCurrency() + " " + milageLog.totalDiscount.toString()
        holder.LocationTextView.text = milageLog.gasStationType
        holder.fuelPerlitreTextView.text =
            milageLog.gas.toString() + "L" + "-" + milageLog.price.toString() + " per litre"
        // holder.distanceTextView.text = milageLog.gas.toString()

        CoroutineScope(Dispatchers.IO).launch {
            vehicleDao.getVehicleData(milageLog.carId).let {
                it?.vehicleName?.let {
                    val carName = it
                    // Switch back to the main thread to update UI
                    CoroutineScope(Dispatchers.Main).launch {
                        holder.carNameTextView.text = carName
                    }
                }

            }
            }



    }

    override fun getItemCount(): Int {
        return array.size
    }

    inner class TripViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val carNameTextView: TextView = itemView.findViewById(R.id.CarnameTextview)
        val pumpNameTextView: TextView = itemView.findViewById(R.id.pumpNameTextview)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextview)
        val priceTextView: TextView = itemView.findViewById(R.id.priceTextview)

        //    val distanceTextView: TextView = itemView.findViewById(R.id.distancetextview)
        val fuelPerlitreTextView: TextView = itemView.findViewById(R.id.fuelPerlitreTextView)
        val LocationTextView: TextView = itemView.findViewById(R.id.LocationTextView)
        val discountTextView: TextView = itemView.findViewById(R.id.discountTextView)
    }
}
