package com.live.streetview.navigation.earthmap.compass.map.activities.game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityTicTacHomeBinding
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityTicTacToeBinding

class TicTacHomeActivity : AppCompatActivity() {

    private var binding : ActivityTicTacHomeBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTicTacHomeBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.singlePlayerBtn?.setOnClickListener {
            val intent = Intent(this, TicTacToe::class.java).apply {
                putExtra("GAME_MODE", "PVC")
            }
            startActivity(intent)
        }

        binding?.multiplPlayer?.setOnClickListener {
            val intent = Intent(this, TicTacToe::class.java).apply {
                putExtra("GAME_MODE", "PVP")
            }
            startActivity(intent)
        }
    }
}