package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.live.streetview.navigation.earthmap.compass.map.databinding.ItemTripLogBinding
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.trip.TripEntity
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.utils.SharedPrefManager
import kotlin.math.cos

class AdapterTripLog(
    private val listener: AdapterListener,
    private val listofTrips: List<TripEntity>,
    private val context: Context
) : RecyclerView.Adapter<AdapterTripLog.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemTripLogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listofTrips.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listofTrips[position])

        holder.binding.ivDeleteTripLog.setOnClickListener {
            listener.onDeleteClicked(position)
        }

        holder.binding.ivEditTripLog.setOnClickListener {
            listener.onEditClicked(position)
        }
    }

    inner class ViewHolder(val binding: ItemTripLogBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tripEntity: TripEntity) {
            // Bind your data to the views using the binding object
            binding.apply {
                tripNameTextview.text = tripEntity.title
                dateTextview.text = tripEntity.startDate
                timeTextview.text = tripEntity.startTime
                distancetextview.text =
                    (if (tripEntity.startOdoCounter != null) tripEntity.finalOdoCounter.toLong() - tripEntity.startOdoCounter.toLong() else 0).toString() + "KM"
                totalCostTextView.text = SharedPrefManager.getInstance(context).getDefaultCurrency() + " " + tripEntity.tripCost.toString()
                startLocationTV.text = tripEntity.startPoint
                endLocationTV.text = tripEntity.endPoint
            }
            // Set other data as needed

        }
    }
}
