package com.live.streetview.navigation.earthmap.compass.map.fuelmanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.live.streetview.navigation.earthmap.compass.map.R

class TripTypeBottomSheetFragment : BottomSheetDialogFragment() {

    interface OnItemClickListener {
        fun onAutoTripClick()
        fun onManualTripClick()
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.select_trip_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textViewAutoTrip = view.findViewById<TextView>(R.id.textViewAutoTrip)
        textViewAutoTrip.setOnClickListener {
            itemClickListener?.onAutoTripClick()
            dismiss()
        }

        val textViewManualTrip = view.findViewById<TextView>(R.id.textViewManualTrip)
        textViewManualTrip.setOnClickListener {
            itemClickListener?.onManualTripClick()
            dismiss()
        }
    }
}
