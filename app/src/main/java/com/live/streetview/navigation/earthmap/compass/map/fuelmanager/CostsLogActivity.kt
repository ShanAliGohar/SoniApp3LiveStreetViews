package com.live.streetview.navigation.earthmap.compass.map.fuelmanager

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityCostsLogBinding
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.adapter.AdapterListener
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.adapter.CostLogAdapter
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.costs.CostDao
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.costs.CostEntity
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.refueling.MileageLogDatabase
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.refueling.MileageLogEntity
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.utils.showDateDialogStartPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class CostsLogActivity : AppCompatActivity(), AdapterListener {

    private lateinit var costDao: CostDao

    lateinit var listCosts: List<CostEntity>

    private lateinit var binding: ActivityCostsLogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCostsLogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.costLogRecycler.layoutManager = LinearLayoutManager(this)


        costDao = MileageLogDatabase.getDatabase(this).costDao()


        binding.btnAddCost.setOnClickListener {
            startActivity(Intent(this, AddCostActivity::class.java))
            finish()
        }

        costDao.getAllCosts().observe(this) { list ->

            listCosts = list
            setAdapter(list)
            binding.btnFilterDate.setOnClickListener {

                val startDate = binding.startDateButton.text.toString()
                val endDate = binding.endDateButton.text.toString()


                if (startDate.contains("2") && endDate.contains("2")) {

                    val filteredList = filterItemsByDateRange(startDate, endDate, list)

                    setAdapter(filteredList)

                } else {
                    Toast.makeText(this, "Select Start & End Date", Toast.LENGTH_SHORT).show()
                }
            }
        }





        binding.startDateButton.setOnClickListener {
            showDateDialogStartPoint(binding.startDateButton)
        }

        binding.endDateButton.setOnClickListener {
            showDateDialogStartPoint(binding.endDateButton)
        }

        binding.imageback.setOnClickListener {
            startActivity(Intent(this@CostsLogActivity, FuelManagerActivity::class.java))
        }



        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@CostsLogActivity, FuelManagerActivity::class.java))
            }
        })

    }




    private fun filterItemsByDateRange(
        startDateString: String,
        endDateString: String,
        itemList: List<CostEntity>
    ): List<CostEntity> {
        val sdf = SimpleDateFormat("dd MMMM, yyyy", Locale.ENGLISH)

        try {
            val startDate = sdf.parse(startDateString)
            val endDate = sdf.parse(endDateString)

            if (startDate != null && endDate != null) {
                return itemList.filter { item ->
                    val itemDate = sdf.parse(item.selectDate)
                    itemDate in startDate..endDate
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return emptyList()
    }

    private fun setAdapter(list: List<CostEntity>) {

        val adapter = CostLogAdapter(this@CostsLogActivity, list)
        binding.costLogRecycler.adapter = adapter


    }

    override fun onEditClicked(position: Int) {

        val intent = Intent(this, EditCostActivity::class.java)
        intent.putExtra("costPosition", position)
        startActivity(intent)

    }


    override fun onDeleteClicked(position: Int) {

        lifecycleScope.launch {
            costDao.deleteCost(listCosts[position])
            Toast.makeText(this@CostsLogActivity, "Deleted Successfully", Toast.LENGTH_SHORT).show()
            setAdapter(listCosts)
        }

    }

    override fun onCarDescriptionClicked(position: Int) {

        // Inflate the dialog layout
        val alertDialogBuilder = AlertDialog.Builder(this)

        // Set dialog title and message
        alertDialogBuilder.setTitle("Cost Note")
        alertDialogBuilder.setMessage(listCosts[position].costNote)

        // Set positive button
        alertDialogBuilder.setPositiveButton("OK") { dialog: DialogInterface?, _: Int ->
            // Positive button click action
            dialog?.dismiss()
        }

        // Show the dialog
        alertDialogBuilder.create().show()


    }
}