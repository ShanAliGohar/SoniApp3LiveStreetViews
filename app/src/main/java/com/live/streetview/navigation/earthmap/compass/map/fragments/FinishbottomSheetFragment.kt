package com.live.streetview.navigation.earthmap.compass.map.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Toast
import com.live.streetview.navigation.earthmap.compass.map.R

class FinishbottomSheetFragment : BottomSheetDialogFragment() {
    var btnexist: Button? = null
    var btncacancel: Button? = null
    var rateUSbar: RatingBar? = null
    var btnClose:ImageView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_finishbottom_sheet, container, false)
    btnexist = view.findViewById(R.id.yesBtn)
        /*  btncacancel = view.findViewById(R.id.btnCancel)
     // rateUSbar = view.findViewById(R.id.rateUSbar)
      btnClose=view.findViewById(R.id.btnClose)*/
        btnexist!!.setOnClickListener { activity?.finishAffinity() }
        btncacancel!!.setOnClickListener { dismiss() }
        btnClose!!.setOnClickListener { dismiss() }

        rateUSbar!!.setOnRatingBarChangeListener { _, _, _ ->

            val barValu = rateUSbar!!.rating

            if (barValu > 3) {
                try {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(getString(R.string.rate_app))
                        )
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        return view
    }
}