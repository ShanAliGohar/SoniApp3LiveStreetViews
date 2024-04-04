package com.live.streetview.navigation.earthmap.compass.map.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.adapters.FavSearchPlaceAdapter
import com.live.streetview.navigation.earthmap.compass.map.adapters.SearchPlaceAdapter
import com.live.streetview.navigation.earthmap.compass.map.callback.onClickItem
import com.live.streetview.navigation.earthmap.compass.map.database.Favourites
import com.live.streetview.navigation.earthmap.compass.map.places.Feature
import com.live.streetview.navigation.earthmap.compass.map.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlin.math.log

@AndroidEntryPoint
class SearchPlacesActivity : AppCompatActivity() {
    var placesRecycler: RecyclerView? = null
    var editTextTextPersonName: EditText? = null
    var textView82: TextView? = null
    var imageView42: ImageView? = null
    private val viewModel: MainViewModel by viewModels()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_places)
        placesRecycler = findViewById(R.id.placesRecycler)
        editTextTextPersonName = findViewById(R.id.editTextTextPersonName)
        textView82 = findViewById(R.id.textView82)
        imageView42 = findViewById(R.id.imageView42)
        placesRecycler!!.layoutManager = LinearLayoutManager(this)
        imageView42!!.setOnClickListener {
            onBackPressed()
        }

        getRecent()
        editTextTextPersonName!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                loadData(s.toString())
            }
        })
        editTextTextPersonName!!.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loadData(editTextTextPersonName!!.text.toString())
            }
            false
        }
    }

    private fun loadData(q: String) {
        textView82!!.visibility = View.INVISIBLE
        CoroutineScope(Dispatchers.Main).launch {
            try {
                var country: String? = null
                var name: String? = null
                var coordinates:  List<Double>? = null
                viewModel.getData(q).catch {
                    Log.d("TAG", "loadDataException: $it")
                    Toast.makeText(this@SearchPlacesActivity, "places name could not be retrieved please try again later", Toast.LENGTH_SHORT).show()
                }.collect {
                    Log.d("TAG", "onCreate: ${it?.features}")
                    if (it != null) {
                        placesRecycler!!.adapter =
                            SearchPlaceAdapter(
                                it.features as ArrayList<Feature>,
                                this@SearchPlacesActivity, object : onClickItem {
                                    override fun click(position: Int) {
                                        try {
                                            CoroutineScope(Dispatchers.IO).launch {
                                                if (it.features != null && it.features.size > position) {
                                                    val feature = it.features[position]
                                                    name = feature.properties.name
                                                    country = feature.properties.country
                                                    coordinates= feature.geometry.coordinates

                                                    // Check for null values before inserting into the database
                                                    if (coordinates!!.isNotEmpty() && name != null && country != null && name!!.isNotEmpty() && country!!.isNotEmpty()) {
                                                        val lat = coordinates!![1]
                                                        val lng = coordinates!![0]

                                                        viewModel.saveDataToDataBase(
                                                            Favourites(
                                                                place = name?:"",
                                                                country = country?:"",
                                                                lat = lat,
                                                                lng = lng
                                                            )
                                                        )

                                                        Log.d(
                                                            "TAGFeature",
                                                            "click: location saved $name"
                                                        )
                                                    } else {
                                                        Log.d(
                                                            "TAGFeature",
                                                            "click: Skipping insertion due to null values in feature: $feature"
                                                        )
                                                    }
                                                }
                                            }

                                            val resultIntent = Intent()
                                            resultIntent.putExtra(
                                                "d_lng",
                                                it?.features?.get(position)?.geometry?.coordinates?.get(
                                                    0
                                                ) ?: 0
                                            )
                                            if (it != null) {
                                                resultIntent.putExtra(
                                                    "d_lat",
                                                    it.features[position].geometry.coordinates[1]
                                                )
                                            }
                                            if (it != null) {
                                                resultIntent.putExtra(
                                                    "place",
                                                    it.features[position].properties.name
                                                )
                                            }
                                            setResult(Activity.RESULT_OK, resultIntent)
                                            finish()
                                        } catch (e: NullPointerException) {
                                        }

                                    }
                                }
                            )
                    }
                }
            } catch (e: NullPointerException) {

            }
        }
    }

    fun getRecent() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getAllFav().catch {
                Log.d("TAG", "getRecent: value is null cant search")
            }.collect {
                Log.d("SIZETAg", "getRecent: ${it.size}")
                if (it.isNotEmpty()) {
                    placesRecycler!!.adapter = FavSearchPlaceAdapter(
                        it as ArrayList<Favourites>,
                        this@SearchPlacesActivity, object : onClickItem {
                            override fun click(position: Int) {
                                val resultIntent = Intent()
                                resultIntent.putExtra(
                                    "d_lng",
                                    it[position].lng
                                )
                                resultIntent.putExtra(
                                    "d_lat",
                                    it[position].lat
                                )
                                resultIntent.putExtra(
                                    "place",
                                    it[position].place
                                )
                                setResult(Activity.RESULT_OK, resultIntent)
                                finish()
                            }
                        })
                }

            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

}