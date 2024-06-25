package com.live.streetview.navigation.earthmap.compass.map.activities.game

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources.Theme
import android.graphics.Color
import android.graphics.Paint.Style
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityTicTacToeBinding
import java.util.Random


class TicTacToe : AppCompatActivity() {

    private lateinit  var button1 : ImageView
    private lateinit  var button2 : ImageView
    private lateinit  var button3 : ImageView
    private lateinit  var button4 : ImageView
    private lateinit  var button5 : ImageView
    private lateinit  var button6 : ImageView
    private lateinit  var button7 : ImageView
    private lateinit  var button8 : ImageView
    private lateinit  var button9 : ImageView


    private lateinit var binding : ActivityTicTacToeBinding

    private var gameMode: String? = null


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTicTacToeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        gameMode = intent.getStringExtra("GAME_MODE")

        openDialogue(this)

        button1 = findViewById(R.id.button1)
        button2 = findViewById(R.id.button2)
        button3 = findViewById(R.id.button3)
        button4 = findViewById(R.id.button4)
        button5 = findViewById(R.id.button5)
        button6 = findViewById(R.id.button6)
        button7 = findViewById(R.id.button7)
        button8 = findViewById(R.id.button8)
        button9 = findViewById(R.id.button9)



        if (gameMode == "PVC") {
            setPlayer = 2
        } else {
            setPlayer = 1
        }

        binding.backBtn.setOnClickListener {
            val intent = Intent(this,TicTacHomeActivity::class.java)
            startActivity(intent)
        }

    }

    private fun openDialogue(context: Context){


        val dialog = Dialog(context, R.style.ThemeOverlay_Material3_MaterialCalendar_Fullscreen)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.tictact_dialouge)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        val imageView60: ImageView = dialog.findViewById(R.id.imageView60)
        val seekBar: SeekBar = dialog.findViewById(R.id.seekBar2)

        val selectBtn: ImageView = dialog.findViewById(R.id.imageView62)
        selectBtn.setOnClickListener {
            dialog.dismiss()
        }

        // Set OnSeekBarChangeListener to change image when progress is 0 or 100
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (progress == 0) {
                    imageView60.setImageDrawable(ContextCompat.getDrawable(this@TicTacToe, R.drawable.tictacsillyface))
                } else if (progress == 100) {
                    imageView60.setImageDrawable(ContextCompat.getDrawable(this@TicTacToe, R.drawable.tictacangryface))
                } else {
                    // Optionally set a default image for other values
                    imageView60.setImageDrawable(ContextCompat.getDrawable(this@TicTacToe, R.drawable.tictacsmileemoji))
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Not needed
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Not needed
            }
        })
    }

    var Player1 = ArrayList<Int>()
    var Player2 = ArrayList<Int>()
    var ActivePlayer = 1
    var setPlayer = 1


    fun restartGame(view: View) {
        button1.setImageDrawable(null)
        button2.setImageDrawable(null)
        button3.setImageDrawable(null)
        button4.setImageDrawable(null)
        button5.setImageDrawable(null)
        button6.setImageDrawable(null)
        button7.setImageDrawable(null)
        button8.setImageDrawable(null)
        button9.setImageDrawable(null)

        binding.horizontalLineOne.visibility = View.INVISIBLE
        binding.horizontalLineTwo.visibility = View.INVISIBLE
        binding.horizontalLineThree.visibility = View.INVISIBLE

        binding.winninglineverticalOne.visibility = View.INVISIBLE
        binding.winninglineverticalTwo.visibility = View.INVISIBLE
        binding.winninglineverticalThree.visibility = View.INVISIBLE

        binding.winninglineDiagonalLeft.visibility = View.INVISIBLE
        binding.winninglineDiagonalRight.visibility = View.INVISIBLE

        Player1.clear()
        Player2.clear()
        ActivePlayer = 1

        button1.isEnabled = true
        button2.isEnabled = true
        button3.isEnabled = true
        button4.isEnabled = true
        button5.isEnabled = true
        button6.isEnabled = true
        button7.isEnabled = true
        button8.isEnabled = true
        button9.isEnabled = true

        // Re-enable touch input
        setButtonsEnabled(true)

        setPlayer = 1

        if (gameMode == "PVC") {
            setPlayer = 2
            AutoPlay()
        } else {
            setPlayer = 1
        }
    }


    fun buttonClick(view: View)
    {
        val buSelected:ImageView = view as ImageView
        var cellId = 0
        when(buSelected.id)
        {
            R.id.button1 -> cellId = 1
            R.id.button2 -> cellId = 2
            R.id.button3 -> cellId = 3

            R.id.button4 -> cellId = 4
            R.id.button5 -> cellId = 5
            R.id.button6 -> cellId = 6
            R.id.button7 -> cellId = 7
            R.id.button8 -> cellId = 8
            R.id.button9 -> cellId = 9
        }
        PlayGame(cellId,buSelected)

    }




    fun PlayGame(cellId: Int, buSelected: ImageView) {
        if (ActivePlayer == 1) {
            buSelected.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.crossglow))
            buSelected.setBackgroundColor(Color.TRANSPARENT)
            Player1.add(cellId)
            ActivePlayer = 2
            if (setPlayer == 1) {
                // Keep buttons enabled for Player vs Player mode
                setButtonsEnabled(true)
            } else {
                try {
                    AutoPlay()
                } catch (ex: Exception) {
                    Toast.makeText(this, "Game Over", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            buSelected.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.triangleglow))
            buSelected.setBackgroundColor(Color.TRANSPARENT)
            Player2.add(cellId)
            ActivePlayer = 1
            // Keep buttons enabled for next player
            setButtonsEnabled(true)
        }
        buSelected.isEnabled = false
        CheckWinner()
    }
    fun setButtonsEnabled(enabled: Boolean) {
        if (!Player1.contains(1) && !Player2.contains(1)) button1.isEnabled = enabled
        if (!Player1.contains(2) && !Player2.contains(2)) button2.isEnabled = enabled
        if (!Player1.contains(3) && !Player2.contains(3)) button3.isEnabled = enabled
        if (!Player1.contains(4) && !Player2.contains(4)) button4.isEnabled = enabled
        if (!Player1.contains(5) && !Player2.contains(5)) button5.isEnabled = enabled
        if (!Player1.contains(6) && !Player2.contains(6)) button6.isEnabled = enabled
        if (!Player1.contains(7) && !Player2.contains(7)) button7.isEnabled = enabled
        if (!Player1.contains(8) && !Player2.contains(8)) button8.isEnabled = enabled
        if (!Player1.contains(9) && !Player2.contains(9)) button9.isEnabled = enabled
    }


    fun CheckWinner()
    {
        var winner = -1

        //row1
        if (Player1.contains(1) && Player1.contains(2) && Player1.contains(3))
        {
            winner = 1
            binding.horizontalLineOne.visibility = View.VISIBLE
        }
        if (Player2.contains(1) && Player2.contains(2) && Player2.contains(3))
        {
            winner = 2
            binding.horizontalLineOne.visibility = View.VISIBLE
        }

        //row2
        if (Player1.contains(4) && Player1.contains(5) && Player1.contains(6))
        {
            winner = 1
            binding.horizontalLineTwo.visibility = View.VISIBLE

        }
        if (Player2.contains(4) && Player2.contains(5) && Player2.contains(6))
        {
            winner = 2
            binding.horizontalLineTwo.visibility = View.VISIBLE

        }

        //row3
        if (Player1.contains(7) && Player1.contains(8) && Player1.contains(9))
        {
            winner = 1
            binding.horizontalLineThree.visibility = View.VISIBLE

        }
        if (Player2.contains(7) && Player2.contains(8) && Player2.contains(9))
        {
            winner = 2
            binding.horizontalLineThree.visibility = View.VISIBLE
        }

        //col1
        if (Player1.contains(1) && Player1.contains(4) && Player1.contains(7))
        {
            winner = 1
            binding.winninglineverticalOne.visibility = View.VISIBLE

        }
        if (Player2.contains(1) && Player2.contains(4) && Player2.contains(7))
        {
            winner = 2
            binding.winninglineverticalOne.visibility = View.VISIBLE


        }

        //col2
        if (Player1.contains(2) && Player1.contains(5) && Player1.contains(8))
        {
            winner = 1
            binding.winninglineverticalTwo.visibility = View.VISIBLE
        }
        if (Player2.contains(2) && Player2.contains(5) && Player2.contains(8))
        {
            winner = 2
            binding.winninglineverticalTwo.visibility = View.VISIBLE

        }

        //col3
        if (Player1.contains(3) && Player1.contains(6) && Player1.contains(9))
        {
            winner = 1
            binding.winninglineverticalThree.visibility = View.VISIBLE

        }
        if (Player2.contains(3) && Player2.contains(6) && Player2.contains(9))
        {
            winner = 2
            binding.winninglineverticalThree.visibility = View.VISIBLE
        }

        //cross1
        if (Player1.contains(1) && Player1.contains(5) && Player1.contains(9))
        {
            winner = 1
            binding.winninglineDiagonalLeft.visibility = View.VISIBLE
        }
        if (Player2.contains(1) && Player2.contains(5) && Player2.contains(9))
        {
            winner = 2
            binding.winninglineDiagonalLeft.visibility= View.VISIBLE

        }

        //cross2
        if (Player1.contains(3) && Player1.contains(5) && Player1.contains(7))
        {
            winner = 1
            binding.winninglineDiagonalRight.visibility= View.VISIBLE

        }
        if (Player2.contains(3) && Player2.contains(5) && Player2.contains(7))
        {
            winner = 2
            binding.winninglineDiagonalRight.visibility= View.VISIBLE

        }

        if (winner != -1)
        {
            if (winner == 1)
            {
                if(setPlayer == 1) {
                    Toast.makeText(this, "Player 1 Wins!!", Toast.LENGTH_SHORT).show()
                    stopTouch()
                }
                else
                {
                    Toast.makeText(this, "You Won!!", Toast.LENGTH_SHORT).show()
                    stopTouch()
                }
            }
            else
            {
                if (setPlayer == 1) {
                    Toast.makeText(this, "Player 2 Wins!!", Toast.LENGTH_SHORT).show()
                    stopTouch()
                }
                else
                {
                    Toast.makeText(this, "CPU Wins!!", Toast.LENGTH_SHORT).show()
                    stopTouch()
                }
            }
        }
    }

    fun stopTouch()
    {
        button1.isEnabled = false
        button2.isEnabled = false
        button3.isEnabled = false
        button4.isEnabled = false
        button5.isEnabled = false
        button6.isEnabled = false
        button7.isEnabled = false
        button8.isEnabled = false
        button9.isEnabled = false
    }

    fun AutoPlay() {
        // Disable touch input for all buttons
        setButtonsEnabled(false)

        val emptyCells = ArrayList<Int>()
        for (cellId in 1..9) {
            if (Player1.contains(cellId) || Player2.contains(cellId)) {
                // Do nothing
            } else {
                emptyCells.add(cellId)
            }
        }

        if (emptyCells.isNotEmpty()) {
            val r = Random()
            val randomIndex = r.nextInt(emptyCells.size)
            val cellId = emptyCells[randomIndex]

            val buSelect: ImageView? = when (cellId) {
                1 -> button1
                2 -> button2
                3 -> button3
                4 -> button4
                5 -> button5
                6 -> button6
                7 -> button7
                8 -> button8
                9 -> button9
                else -> button1
            }

            // Add a delay of 2 seconds
            Handler(Looper.getMainLooper()).postDelayed({
                PlayGame(cellId, buSelect!!)
                // Keep buttons disabled for next player
                // Disable the selected button
                buSelect.isEnabled = false
            }, 2000)
        } else {
            // Re-enable touch input if there are no empty cells
            setButtonsEnabled(true)
        }
    }



}
