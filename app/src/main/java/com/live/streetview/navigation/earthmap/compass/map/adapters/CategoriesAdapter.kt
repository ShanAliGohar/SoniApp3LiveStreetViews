package com.live.streetview.navigation.earthmap.compass.map.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.live.streetview.navigation.earthmap.compass.map.Model.nearby.Category
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.callback.NearByActivityCallBack

class CategoriesAdapter(
    private val context: Context,
    private val listOfCategories: List<Category>,
    private val clickListener:NearByActivityCallBack
) : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_category, parent, false), context,clickListener
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(listOfCategories[position])
    }

    override fun getItemCount(): Int {
        return listOfCategories.size
    }

    class CategoryViewHolder(
        private val view: View,
        val context: Context,
       val  clickListener: NearByActivityCallBack
    ) : RecyclerView.ViewHolder(view) {
        fun bind(category: Category) {
            view.findViewById<TextView>(R.id.categoryName).text = category.name
            val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
            recyclerView.layoutManager = GridLayoutManager(context, 3)
            recyclerView.adapter = ItemsAdapter(view.context, category.listOfItems,clickListener)
        }
    }
}
