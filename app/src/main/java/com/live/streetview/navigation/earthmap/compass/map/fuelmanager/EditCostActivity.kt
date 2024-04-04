package com.live.streetview.navigation.earthmap.compass.map.fuelmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityAddCostBinding
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityEditCostBinding
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.costs.CostDao
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.costs.CostEntity
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.refueling.MileageLogDatabase
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.vehicle.VehicleDatabase
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.utils.showDateDialogEndPoint
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.utils.showTimeDialogEndPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.cos

class EditCostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditCostBinding

    private lateinit var costEntity: CostEntity

    private lateinit var costsDao: CostDao

    private var serviceType: String? = null
    private var carSelected: String? = null
    private var recurrence: String? = null

    private var remainderType: String? = null
    private var remainderOdometer: Int? = null
    private var remainderDate: String? = null
    private var isRecurringRemainder: Boolean? = null
    private var remainderRepeatDistance: Int? = null
    private var remainderRepeatMonths: Int? = null
    private var isRemainderDone: Boolean? = null

    private val serviceList = listOf(
        "Service",
        "Maintenance",
        "Registration",
        "Parking",
        "Wash",
        "Tolls",
        "Ticket/fine",
        "Tuning",
        "Insurance"
    )
    val recurrenceList = listOf("One Time", "Monthly")


    private lateinit var costData: CostEntity

    private var selectedCar: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditCostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        costsDao = MileageLogDatabase.getDatabase(this).costDao()

        // Add similar lists for other spinners

        binding.spinnerSelectService.adapter = ArrayAdapter(
            this, R.layout.custom_spinner_dropdown_costs, R.id.customSpinnerItemText, serviceList
        )
        binding.spinnerSelectRecurrence.adapter = ArrayAdapter(
            this, R.layout.custom_spinner_dropdown_costs, R.id.customSpinnerItemText, recurrenceList
        )


        // Add similar adapter setups for other spinners

        // Set up listeners for spinners
        setupSpinnerListener(binding.spinnerSelectService, "Selected Service")
        setupSpinnerListener(binding.spinnerSelectRecurrence, "Select Recurrence")
        // Add similar listener setups for other spinners

        spinnerCarselect()

        setUpRemainder()


        binding.imageViewBack.setOnClickListener {
            startActivity(Intent(this, CostsLogActivity::class.java))
            finish()
        }

        binding.cardSelectDate.setOnClickListener {
            showDateDialogEndPoint(binding.tvSelectDate)
        }

        binding.cardSelectTime.setOnClickListener {
            showTimeDialogEndPoint(binding.tvSelectTime)
        }


        binding.switchSelectReminder.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // If switch is checked (ON), show the reminder details layout
                binding.cardSetReminder.visibility = View.VISIBLE
            } else {
                // If switch is unchecked (OFF), hide the reminder details layout
                binding.cardSetReminder.visibility = View.GONE
            }
        }

        // Set up listener for the "Add" button
        binding.btnAddCost.setOnClickListener {
            // Handle the click event
            // Access selected items using view binding if needed
            saveCost()
        }

        setIntentCostData()
    }


    private fun setIntentCostData() {
        val position = intent.getIntExtra("costPosition", 0)

        lifecycleScope.launch {
            costsDao.getAllCosts().observe(this@EditCostActivity) { list ->


                costData = list[position]

                binding.apply {
                    editTextCostTitle.setText(costData.costTitle)
                    editTextCostNote.setText(costData.costNote)
                    editTextOdometer.setText(costData.reminderOdometer.toString())
                    editTextTotalCost.setText(costData.totalCost.toString())
                    tvSelectDate.text = costData.selectDate
                    tvSelectTime.text = costData.selectTime
                    isRecurringRemainder = costData.reminderRecurring
                    tvSelectReminderDate.text = costData.reminderDate
                    editTextOdometerRepeatDistance.setText(costData.reminderRepeatDistance.toString())
                    editTextOdometerRepeatMonths.setText(costData.reminderRepeatMonths.toString())
                    checkBoxDone.isActivated = costData.reminderDone == false

                    // Use the correct lists for setting spinner positions
                    spinnerSelectRecurrence.setSelection(recurrenceList.indexOf(costData.recurrence))
                    spinnerSelectService.setSelection(serviceList.indexOf(costData.service))

                    lifecycleScope.launch {

                        val vehicleDao =
                            VehicleDatabase.getDatabase(this@EditCostActivity).vehicleDao()

                        vehicleDao.getAllVehicleData().observe(this@EditCostActivity) { vehicles ->
                            val carsList = vehicles.map { it.vehicleName }

                            spinnerSelectCar.setSelection(carsList.indexOf(costData.car))
                        }
                    }
                }
            }
        }
    }


    private fun spinnerCarselect() {
        CoroutineScope(Dispatchers.Main).launch {
            val vehicleDao = VehicleDatabase.getDatabase(this@EditCostActivity).vehicleDao()

            vehicleDao.getAllVehicleData().observe(this@EditCostActivity) { vehicles ->
                val list = vehicles.map { it.vehicleName }

                binding.spinnerSelectCar.adapter = ArrayAdapter(
                    this@EditCostActivity,
                    R.layout.custom_spinner_dropdown_costs,
                    R.id.customSpinnerItemText,
                    list
                )

                // Set an OnItemSelectedListener to respond to item selection
                setupSpinnerListener(binding.spinnerSelectCar, "Selected Car")
            }
        }
    }


    private fun saveCost() {
        costsDao = MileageLogDatabase.getDatabase(this).costDao()

        costEntity = CostEntity(
            id = costData.id,
            car = selectedCar ?: binding.spinnerSelectCar.selectedItem.toString(),
            service = serviceType ?: binding.spinnerSelectService.selectedItem.toString(),
            reminder = binding.switchSelectReminder.isChecked,
            costTitle = binding.editTextCostTitle.text.toString(),
            totalCost = binding.editTextTotalCost.text.toString().toDouble(),
            selectDate = binding.tvSelectDate.text.toString(),
            selectTime = binding.tvSelectTime.text.toString(),
            costNote = binding.editTextCostNote.text.toString(),
            recurrence = recurrence ?: binding.spinnerSelectRecurrence.selectedItem.toString(),
            reminderType = remainderType,
            reminderOdometer = remainderOdometer,
            reminderDate = remainderDate,
            reminderRecurring = isRecurringRemainder,
            reminderRepeatDistance = remainderRepeatDistance,
            reminderRepeatMonths = remainderRepeatMonths,
            reminderDone = isRemainderDone
        )

        Toast.makeText(
            this,
            binding.spinnerSelectService.selectedItem as String,
            Toast.LENGTH_SHORT
        ).show()

        lifecycleScope.launch {
            costsDao.updateCost(costEntity)
            startActivity(Intent(this@EditCostActivity, CostsLogActivity::class.java))
        }
    }


    private fun setupSpinnerListener(spinner: Spinner, label: String) {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long
            ) {
                // Handle the selected item
                val selectedItem = spinner.selectedItem.toString()
                println("$label: $selectedItem")


                if (label == "Selected Service") {
                    serviceType = spinner.selectedItem.toString()
                }
                if (label == "Select Recurrence") {
                    recurrence = spinner.selectedItem.toString()
                    Toast.makeText(this@EditCostActivity, recurrence, Toast.LENGTH_SHORT).show()
                }
                if (label == "Selected Car") {
                    carSelected = spinner.selectedItem.toString()
                }

                // Add logic as needed
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // Do nothing here
            }
        }
    }


    private fun setUpRemainder() {


        binding.apply {

            btnSpecificReminder.setOnClickListener {

                btnSpecificReminder.setBackgroundColor(resources.getColor(R.color.blue))
                btnRemindInReminder.setBackgroundColor(resources.getColor(R.color.clrBgFuelManager))

                remainderType = "Specific"

            }

            btnRemindInReminder.setOnClickListener {

                btnSpecificReminder.setBackgroundColor(resources.getColor(R.color.clrBgFuelManager))
                btnRemindInReminder.setBackgroundColor(resources.getColor(R.color.blue))


                remainderType = "Remind In"

            }

            cardSelectReminderDate.setOnClickListener {
                showDateDialogEndPoint(binding.tvSelectReminderDate)
            }

            cardSetRemainderDone.setOnClickListener {

                remainderOdometer = binding.editTextOdometer.text.toString().toIntOrNull()
                remainderDate = binding.tvSelectReminderDate.text.toString()
                remainderRepeatMonths =
                    binding.editTextOdometerRepeatMonths.text.toString().toIntOrNull()
                remainderRepeatDistance =
                    binding.editTextOdometerRepeatDistance.text.toString().toIntOrNull()
                isRemainderDone = binding.checkBoxDone.isChecked
                isRecurringRemainder = binding.switchSelectRecurringReminder.isChecked

            }

        }


    }


}