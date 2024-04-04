package com.live.streetview.navigation.earthmap.compass.map.Model.nearby

class Category(val name: String, vararg item: Item) {

    val listOfItems: List<Item> = item.toList()

}