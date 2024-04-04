package com.live.streetview.navigation.earthmap.compass.map.dialog


import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.live.streetview.navigation.earthmap.compass.map.activities.utils.UtilityClass
import com.live.streetview.navigation.earthmap.compass.map.callback.PremiumCallBack
import com.live.streetview.navigation.earthmap.compass.map.databinding.PremiumDialogBinding

class PremiumDialogueBox(private var mContext: Context, private val callBack: PremiumCallBack):
    Dialog(mContext) {
    lateinit var binding: PremiumDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(true)
          window!!.requestFeature(Window.FEATURE_NO_TITLE)
          window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
          binding=PremiumDialogBinding.inflate(layoutInflater)
          setContentView(binding.root)

        UtilityClass.canPremiumShow=false

//          binding.textView14.setOnClickListener {
//              callBack.onBuyPremium()
//              dismiss()
//          }

    }

}