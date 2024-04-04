package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.fuelprices.fuel_db_adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.live.streetview.navigation.earthmap.compass.map.databinding.CountryListItemsBinding
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.fuelprices.dataClasses.Country
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.fuelprices.interfaces.CountryListener

class CountryListAdapter(
    private var countryList: ArrayList<Country>?,
    private val countryListener: CountryListener
) :
    RecyclerView.Adapter<CountryListAdapter.ViewHolder>() {
    var countrySelectedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        // Binding Created
        val bindView = CountryListItemsBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(bindView)
    }

    override fun getItemCount(): Int {
        return countryList?.size ?: 0
    }

    @SuppressLint("ResourceAsColor", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val countryList = countryList?.get(position)
        with(holder.binding) {
            countryTv.text = countryList?.name

            /*    val flagString = "https://flagpedia.net/data/flags/normal/" +
                        countryList?.alpha2Code?.lowercase(Locale.US) +
                        ".png"
                try {
                    Glide.with(countryTv.context).load(flagString).listener(object :
                        RequestListener<Drawable?> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any,
                            target: Target<Drawable?>,
                            isFirstResource: Boolean,
                        ): Boolean {
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any,
                            target: Target<Drawable?>,
                            dataSource: DataSource,
                            isFirstResource: Boolean,
                        ): Boolean {
                            return false
                        }
                    }).into(flagIcon)
                } catch (e: Exception) {
                    e.printStackTrace()
                }*/

            // Setting the checked state of the radio button based on the selected position
            //radioButton.isChecked = (position == countrySelectedPosition)
            countryConst.setOnClickListener {
                countrySelectedPosition = position
                notifyDataSetChanged()
                countryList?.name.let { it1 ->
                    if (it1 != null) {
                        countryListener.onClick(position, it1)
                    }
                }
            }
        }
    }

    class ViewHolder(val binding: CountryListItemsBinding) : RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: ArrayList<Country>) {
        countryList = newList
        notifyDataSetChanged()
    }
}