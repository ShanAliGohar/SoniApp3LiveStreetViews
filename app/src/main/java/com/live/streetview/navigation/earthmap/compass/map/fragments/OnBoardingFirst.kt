package com.live.streetview.navigation.earthmap.compass.map.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.databinding.FragmentOnBoardingFirstBinding

class OnBoardingFirst : Fragment() {
    private val binding: FragmentOnBoardingFirstBinding by lazy {
        FragmentOnBoardingFirstBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return binding.root
    }

}