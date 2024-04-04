package com.live.streetview.navigation.earthmap.compass.map.adapters.fuel_db_adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.live.streetview.navigation.earthmap.compass.map.fuelWork.dataClasses.FuelDataList
import com.live.streetview.navigation.earthmap.compass.map.databinding.PricesListItemsBinding

class PricesAdapter(
    private val pricesList: ArrayList<FuelDataList>?,
  val  s: String
) : RecyclerView.Adapter<PricesAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        // Binding Created
        val bindView = PricesListItemsBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(bindView)
    }

    override fun getItemCount(): Int {
        return pricesList?.size ?: 0
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fuelDataPrice = pricesList?.get(position)
        with(holder.binding) {
            fuelNameTv.text = fuelDataPrice?.name.toString()
            priceTv.text = fuelDataPrice?.price.toString()

            literTv.text = s
        }
    }

    class ViewHolder(val binding: PricesListItemsBinding) : RecyclerView.ViewHolder(binding.root)
}