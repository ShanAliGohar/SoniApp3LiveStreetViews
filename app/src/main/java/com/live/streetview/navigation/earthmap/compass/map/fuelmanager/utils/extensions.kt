package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private var months = arrayOf(
    "January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
)

@SuppressLint("SetTextI18n")
fun Activity.showDateDialogStartPoint(startDateBtn: TextView) {
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(this, { _, year, monthOfYear, dayOfMonth ->
        startDateBtn.text = "$dayOfMonth ${months[monthOfYear]}, $year"
    }, year, month, day)

    datePickerDialog.show()
}

@SuppressLint("SetTextI18n")
fun Activity.showDateDialogEndPoint(endDateBtn: TextView) {
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(this, { _, year, monthOfYear, dayOfMonth ->
        endDateBtn.text = "$dayOfMonth ${months[monthOfYear]}, $year"
    }, year, month, day)

    datePickerDialog.show()
}

@SuppressLint("SetTextI18n")
fun Activity.showTimeDialogStartPoint(startTimeBtn: TextView) {
    val c = Calendar.getInstance()
    val hour = c.get(Calendar.HOUR_OF_DAY)
    val minute = c.get(Calendar.MINUTE)

    val timePickerDialog = TimePickerDialog(
        this,
        { _, selectedHour, selectedMinute ->
            startTimeBtn.text = formatTime(selectedHour, selectedMinute)
        },
        hour,
        minute,
        true
    )

    timePickerDialog.show()
}

@SuppressLint("SetTextI18n")
fun Activity.showTimeDialogEndPoint(endTimeBtn: TextView) {
    val c = Calendar.getInstance()
    val hour = c.get(Calendar.HOUR_OF_DAY)
    val minute = c.get(Calendar.MINUTE)

    val timePickerDialog = TimePickerDialog(
        this,
        { _, selectedHour, selectedMinute ->
            endTimeBtn.text = formatTime(selectedHour, selectedMinute)
        },
        hour,
        minute,
        true
    )

    timePickerDialog.show()
}

private fun formatTime(hour: Int, minute: Int): String {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.MINUTE, minute)
    return timeFormat.format(calendar.time)
}


fun showToast(context: Context, message: String) {

    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
