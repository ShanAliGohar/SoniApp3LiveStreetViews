package com.live.streetview.navigation.earthmap.compass.map.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.live.streetview.navigation.earthmap.compass.map.Model.nearby.Item
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.callback.NearByActivityCallBack

class ItemsAdapter(
    private val context: Context,
    private var items: List<Item>,
    private val clickListener: NearByActivityCallBack
) :
    RecyclerView.Adapter<ItemsAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener {
            clickListener.onClick(items[position].content)
//            Log.d("TAG", "onBindViewHolder:${items.get(position).content} ")
//            Toast.makeText(context, "click", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ItemViewHolder(private val view: View) :
        RecyclerView.ViewHolder(view) {

        @SuppressLint("ResourceAsColor")
        fun bind(item: Item) {

            view.findViewById<TextView>(R.id.txtPlaceName).text = item.content
            Glide.with(view.context).load(item.image).into( view.findViewById<ImageView>(R.id.imagePlace))
//            view.findViewById<ImageView>(R.id.imagePlace).setImageResource(item.image)
            populateColor(item.content)

        }

        fun populateColor(title: String) {
            if (title == "Gas") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#FF8E8E"))

            } else if (title == "Hospital") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#FFC72D"))

            } else if (title == "School") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#FFC72D"))

            } else if (title == "Airport") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#20746E"))

            } else if (title == "Hotel") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#E25151"))

            } else if (title == "FastFood") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#E25151"))

            } else if (title == "ATM") {

                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#8FC845"))
            } else if (title == "Coffee") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#C547D4"))

            } else if (title == "Worship") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#FF8E8E"))

            } else if (title == "Fire") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#20746E"))

            } else if (title == "Gym") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#FFC72D"))

            } else if (title == "Library") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#FF8E8E"))

            } else if (title == "Museum") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#8FC845"))

            } else if (title == "Bakery") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#20746E"))

            } else if (title == "Movie") {

                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#FF8E8E"))
            } else if (title == "Grocery") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#8FC845"))
            } else if (title == "Bank") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#FF8E8E"))
            } else if (title == "Court") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#FFC72D"))
            } else if (title == "College&University") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#FF8E8E"))
            } else if (title == "Restaurant") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#8FC845"))
            } else if (title == "Beer bar") {

                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#FFC72D"))
            } else if (title == "Nightlife spot") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#FF8E8E"))

            } else if (title == "Cafeteria") {

                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#6495ED"))
            } else if (title == "Grocery store") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#FF8E8E"))

            } else if (title == "Department store") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#FFC72D"))

            } else if (title == "Electronics store") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#20746E"))

            } else if (title == "Hardware store") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#E25151"))

            } else if (title == "Events") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#FF8E8E"))

            } else if (title == "Flower store") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#6495ED"))

            } else if (title == "Shoe store") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#20746E"))

            } else if (title == "Shopping mall") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#FF8E8E"))

            } else if (title == "Clothing store") {

                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#E25151"))
            } else if (title == "Pet supplies store") {

                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#6495ED"))
            } else if (title == "Automotive Service") {

                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#20746E"))
            } else if (title == "Car wash") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#E25151"))

            } else if (title == "Home services") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#FF8E8E"))

            } else if (title == "Fuel station") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#FF8E8E"))

            } else if (title == "Painter") {

                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#E25151"))
            } else if (title == "Parking") {

                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#FF8E8E"))
            } else if (title == "Banks") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#6495ED"))

            } else if (title == "Government office") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#FF8E8E"))

            } else if (title == "Police station") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#FFC72D"))

            } else if (title == "Post office") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#E25151"))

            } else if (title == "Rail station") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#20746E"))

            } else if (title == "Embassy") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#6495ED"))

            } else if (title == "Real estate") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#E25151"))

            } else if (title == "Doctor") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#20746E"))

            } else if (title == "Dentist") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#FFC72D"))

            } else if (title == "Medical center") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#C547D4"))

            } else if (title == "Out Door Gym") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#FFC72D"))

            } else if (title == "Physical Therapist") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#59B4A7"))

            } else if (title == "Mosque") {

                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#E25151"))
            } else if (title == "Temple") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#FF8E8E"))

            } else if (title == "Buddhist temple") {

                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#C547D4"))
            } else if (title == "Confucian temple") {

                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#FF8E8E"))
            } else if (title == "Hindu temple") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#59B4A7"))

            } else if (title == "Sikh temple") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#FFC72D"))

            } else if (title == "Church") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#FF8E8E"))

            } else if (title == "art gallery") {

                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#E25151"))
            } else if (title == "bowling alley") {

                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#C547D4"))
            } else if (title == "bus station") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#59B4A7"))

            } else if (title == "Music venue") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#FF8E8E"))

            } else if (title == "Laundry") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#FF8E8E"))

            } else if (title == "Courthouse") {

                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#FF8E8E"))
            } else if (title == "Zoo") {

                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#C547D4"))
            } else if (title == "movie theater") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#FFC72D"))

            } else if (title == "casino") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#59B4A7"))

            } else if (title == "stadium") {

                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#FFC72D"))
            } else if (title == "boutique") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#C547D4"))

            } else if (title == "Beach") {

                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#FF8E8E"))
            } else if (title == "Bike trail") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#59B4A7"))

            } else if (title == "Swimming Pool") {
                view.findViewById<CardView>(R.id.cardMain)
                    .setCardBackgroundColor(Color.parseColor("#FFC72D"))

            }
        }
    }


}


