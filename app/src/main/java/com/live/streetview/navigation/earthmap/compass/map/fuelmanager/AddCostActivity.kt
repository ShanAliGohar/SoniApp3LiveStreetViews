package com.live.streetview.navigation.earthmap.compass.map.fuelmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityAddCostBinding
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.costs.CostEntity
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.refueling.MileageLogDatabase
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.vehicle.VehicleDatabase
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.utils.AlarmHelper
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.utils.showDateDialogEndPoint
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.utils.showDateDialogStartPoint
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.utils.showTimeDialogEndPoint
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.cos

class AddCostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddCostBinding

    private lateinit var costEntity: CostEntity


    private var remainderType: String? = null
    private var remainderOdometer: Int? = null
    private var remainderDate: String? = null
    private var isRecurringRemainder: Boolean? = null
    private var remainderRepeatDistance: Int? = null
    private var remainderRepeatMonths: Int? = null
    private var isRemainderDone: Boolean? = null

    private var selectedCar: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCostBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val serviceList = listOf(
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


        binding.cardSelectDate.setOnClickListener {
            showDateDialogEndPoint(binding.tvSelectDate)
        }

        binding.cardSelectTime.setOnClickListener {
            showTimeDialogEndPoint(binding.tvSelectTime)
        }


        binding.tvSelectReminderDate.setOnClickListener {
            showDateDialogEndPoint(binding.tvSelectReminderDate)
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
        binding.textViewSelectReminderDate.setOnClickListener {
            showDateDialogStartPoint(binding.textViewSelectReminderDate)
        }


        spinnerCarselect()

        setUpRemainder()

    }


    private fun spinnerCarselect() {
        CoroutineScope(Dispatchers.Main).launch {
            val vehicleDao = VehicleDatabase.getDatabase(this@AddCostActivity).vehicleDao()

            vehicleDao.getAllVehicleData().observe(this@AddCostActivity) { vehicles ->
                val list = vehicles.map { it.vehicleName }

                binding.spinnerSelectCar.adapter = ArrayAdapter(
                    this@AddCostActivity,
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
        val costsDao = MileageLogDatabase.getDatabase(this).costDao()

        val selectedCar = binding.spinnerSelectCar.selectedItem as String
        val selectedService = binding.spinnerSelectService.selectedItem as String
        val selectedRecurrence = binding.spinnerSelectRecurrence.selectedItem as String
        val costTitle = binding.editTextCostTitle.text.toString()
        val totalCostText = binding.editTextTotalCost.text.toString()
        val selectDate = binding.tvSelectDate.text.toString()
        val selectTime = binding.tvSelectTime.text.toString()


        // Check for null or empty values
        if (selectedCar.isEmpty()) {
            showToast(this, "Please select a car")
            return
        }

        if (selectedService.isEmpty()) {
            showToast(this, "Please select a service")
            return
        }

        if (selectedRecurrence.isEmpty()) {
            showToast(this, "Please select a recurrence")
            return
        }

        if (costTitle.isEmpty()) {
            showToast(this, "Please enter a cost title")
            return
        }

        if (totalCostText.isEmpty()) {
            showToast(this, "Please enter the total cost")
            return
        }

        if (selectDate.isEmpty()) {
            showToast(this, "Please select a date")
            return
        }

        if (selectTime.isEmpty()) {
            showToast(this, "Please select a time")
            return
        }

        // Convert totalCost to double or use default value if conversion fails
        val totalCost = totalCostText.toDoubleOrNull() ?: 0.0

        costEntity = CostEntity(
            car = selectedCar,
            service = selectedService,
            reminder = binding.switchSelectReminder.isChecked,
            costTitle = costTitle,
            totalCost = totalCost,
            selectDate = selectDate,
            selectTime = selectTime,
            costNote = binding.editTextCostNote.text.toString(),
            recurrence = selectedRecurrence,
            reminderType = if (binding.switchSelectReminder.isChecked) remainderType else null,
            reminderOdometer = if (binding.switchSelectReminder.isChecked) remainderOdometer else null,
            reminderDate = if (binding.switchSelectReminder.isChecked) remainderDate else null,
            reminderRecurring = if (binding.switchSelectReminder.isChecked) isRecurringRemainder else null,
            reminderRepeatDistance = if (binding.switchSelectReminder.isChecked) remainderRepeatDistance else null,
            reminderRepeatMonths = if (binding.switchSelectReminder.isChecked) remainderRepeatMonths else null,
            reminderDone = if (binding.switchSelectReminder.isChecked) isRemainderDone else null
        )


        Log.d("saveddd", "saveCost: $costEntity")

        if (binding.switchSelectReminder.isChecked) {
            val selectedDate = binding.textViewSelectReminderDate.text.toString()
            // val selectedTime = binding.tvSelectTime.text.toString()

            val pattern = SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault())
            val date = pattern.parse(selectedDate)

            val calendar = Calendar.getInstance()
            if (date != null) {
                calendar.time = date
            }

            // val hour = selectedTime.substring(0, 2).toInt()

            val alarmTimeInMillis = getAlarmTimeAtMillis(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                12
            )
            AlarmHelper(this).setAlarm(
                costEntity.hashCode(),
                costEntity.costTitle,
                costEntity.costNote,
                alarmTimeInMillis,
                costEntity.reminderRepeatMonths,
                costEntity.reminderRecurring!!
            )
        }

        lifecycleScope.launch {
            costsDao.insertCost(costEntity)
            startActivity(Intent(this@AddCostActivity, CostsLogActivity::class.java))
            finish()
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

                Toast.makeText(this@AddCostActivity, selectedItem.toString(), Toast.LENGTH_SHORT)
                    .show()

                // Add logic as needed
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // Do nothing here
            }
        }
    }


    private fun getAlarmTimeAtMillis(year: Int, month: Int, dayOfMonth: Int, hour: Int): Long {


        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        return AlarmHelper.calculateTriggerTime(calendar)

    }


    private fun setUpRemainder() {


        binding.apply {

            btnSpecificReminder.setOnClickListener {

                btnSpecificReminder.isSelected = true
                btnRemindInReminder.isSelected = false

                /*    btnSpecificReminder.setBackgroundColor(resources.getColor(R.color.blue))
                    btnRemindInReminder.setBackgroundColor(resources.getColor(R.color.clrBgFuelManager))
    */
                remainderType = "Specific"

            }

            btnRemindInReminder.setOnClickListener {

                /*     btnSpecificReminder.setBackgroundColor(resources.getColor(R.color.clrBgFuelManager))
                     btnRemindInReminder.setBackgroundColor(resources.getColor(R.color.blue))
     */

                btnSpecificReminder.isSelected = false
                btnRemindInReminder.isSelected = true

                remainderType = "Remind In"

            }



            cardSetRemainderDone.setOnClickListener {

                remainderOdometer = binding.editTextOdometer.text.toString().toIntOrNull()
                remainderDate = binding.textViewSelectReminderDate.text.toString()
                remainderRepeatMonths =
                    binding.editTextOdometerRepeatMonths.text.toString().toIntOrNull()
                remainderRepeatDistance =
                    binding.editTextOdometerRepeatDistance.text.toString().toIntOrNull()
                isRemainderDone = binding.checkBoxDone.isChecked
                isRecurringRemainder = binding.switchSelectRecurringReminder.isChecked


                binding.cardSetReminder.visibility = View.GONE

            }
        }
    }


}