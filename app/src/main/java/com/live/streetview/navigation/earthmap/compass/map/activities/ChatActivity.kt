package com.live.streetview.navigation.earthmap.compass.map.activities

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.deimos.openaiapi.OpenAI
import com.google.android.gms.ads.AdView
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniBillingHelper
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds
import com.live.streetview.navigation.earthmap.compass.map.adapters.MessageAdapter
import com.live.streetview.navigation.earthmap.compass.map.database.ChatRecord
import com.live.streetview.navigation.earthmap.compass.map.database.NewChat
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityChatBinding
import com.live.streetview.navigation.earthmap.compass.map.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private val viewModel: MainViewModel by viewModels()
    val dataList = ArrayList<ChatRecord>()
    var chat: NewChat? = null
    var id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            val key = intent.extras!!.getInt("key")
            if (key == null) {
                getRecent()
                Log.d("TAG", "onCreate: new chat1")
            } else {
                id = key
                getChatHistory()
                Log.d("TAG", "onCreate: recent chat")
            }
        } catch (e: java.lang.NullPointerException) {
            getRecent()
            Log.d("TAG", "onCreate: new chat")
        }


//        populateAdapter()
//        val API_KEY = "Bearer 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"
        val API_KEY = "Bearer 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"
        val openAI = OpenAI(API_KEY)
        var prompt =
            "The following is a conversation with an AI assistant. The assistant is helpful, creative, clever, and very friendly."
        binding.imageView45.setOnClickListener {
            onBackPressed()
        }
        binding.btnSend.setOnClickListener {
            binding.textView85.text="typing..."
            val currentDate: String =
                SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
            val message = binding.etMessage.text.toString()
            deleteTVMessage()
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.saveDataToDataBaseChatHistory(
                    ChatRecord(
                        null,
                        message,
                        currentDate,
                        MessageAdapter.VIEW_TYPE_ONE,
                        id
                    )
                )
                delay(1000)
                getChatHistory()
            }
//            binding.messageRecycler.adapter!!.notifyDataSetChanged()
            hideKeyboard()
            CoroutineScope(Dispatchers.IO).launch {
                prompt += "\n\nHuman: $message \nAI:"
                try {
                    val response = openAI.createCompletion(
                        model = "text-davinci-003",
                        prompt = prompt,
                        temperature = 0.9,
                        max_tokens = 150,
                        top_p = 1,
                        frequency_penalty = 0.0,
                        presence_penalty = 0.6,
                        stop = listOf(" Human:", " AI:")
                    )

                    if (response.isSuccessful) {
                        var answer = response.body()?.choices?.first()?.text
                        answer = answer?.trimStart() // Delete the first space from the answer
                        Log.d("RESPONSE", "Error: ${response.code()} ${answer}")
                        CoroutineScope(Dispatchers.Main).launch {
                            viewModel.saveDataToDataBaseChatHistory(
                                ChatRecord(
                                    null,
                                    answer!!,
                                    currentDate,
                                    MessageAdapter.VIEW_TYPE_TWO,
                                    id
                                )
                            )
                            delay(1500)
                            binding.textView85.text="online"
                            getChatHistory()
                        }

                    } else {
                        Log.d("RESPONSE", "Error: ${response.code()} ${response.message()}")
                    }
                } catch (e: Exception) {
                    Log.d("RESPONSE", "Error: $e")
                }
            }
        }
        binding.tvResponse.setOnClickListener { hideKeyboard() }

    }

    private fun deleteTVMessage() {
        binding.etMessage.setText("")
    }
    private fun hideKeyboard() {
        val activityView = this.window.decorView
        val imm =
            activityView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activityView.windowToken, 0)
    }


//    override fun onBackPressed() {
//        StreetViewAppSoniMyAppShowAds.mediationBackPressedSimpleStreetViewLocation(
//            this,
//            StreetViewAppSoniMyAppAds.admobInterstitialAd,
//            binding.
//        )
//    }

    fun getRecent() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getLastRecord().collect {
                chat = it
                id = chat!!.id!!.toInt()
                Log.d("TAG", "getRecent: ${id}")
            }
        }
    }

    fun getChatHistory() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getAllChatHistory(id).collect {
                val linearLayoutManager: LinearLayoutManager =
                    LinearLayoutManager(this@ChatActivity);
                binding.messageRecycler.layoutManager = linearLayoutManager
                binding.messageRecycler.adapter = MessageAdapter(
                    this@ChatActivity,
                    it as ArrayList<ChatRecord>,
                )
                Log.d("TAG", "getChatHistory: ${it}")
                binding.messageRecycler.scrollToPosition(binding.messageRecycler.adapter!!.itemCount - 1)


            }
        }
    }

}