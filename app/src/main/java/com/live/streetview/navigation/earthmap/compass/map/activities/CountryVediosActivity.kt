package com.live.streetview.navigation.earthmap.compass.map.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.live.streetview.navigation.earthmap.compass.map.Model.VedModel
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.Webcam.WebCamModel
import com.live.streetview.navigation.earthmap.compass.map.Wevcamapi.WebCamApi
import com.live.streetview.navigation.earthmap.compass.map.Wevcamapi.WebcamRetrofitInstance
import com.live.streetview.navigation.earthmap.compass.map.adapters.VedAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CountryVediosActivity : AppCompatActivity() {
    var recyclerViewcon: RecyclerView? = null
    var vedModel: VedModel? = null
    var list = ArrayList<VedModel>()
    var vedAdapter: VedAdapter? = null
    var webCamModel: WebCamModel? = null
    var apikey = "12ybipZNQOzTVzK7Sxy9vrSkQqMYZpXM"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_vedios)
        recyclerViewcon = findViewById(R.id.countryrecy)
        val alpha2code = intent.getStringExtra("alpha2code")
        //        String flagUrl = getIntent().getStringExtra("flag");
        showWebcamData(alpha2code)
    }

    private fun showWebcamData(alpha2code: String?) {
        val webCamApi = WebcamRetrofitInstance.getRetrofitInstance().create(
            WebCamApi::class.java
        )
        val cloudsCall =
            webCamApi.getWebCamFromApi(alpha2code, 20, apikey, "webcams:player,image,live")
        cloudsCall.enqueue(object : Callback<WebCamModel?> {
            override fun onResponse(call: Call<WebCamModel?>, response: Response<WebCamModel?>) {
                if (response.isSuccessful) {
                    webCamModel = response.body()
                    val vedModel = VedModel()
                    var data = 0
                    while (webCamModel!!.result?.webcams?.size!! > data) {
                        vedModel.textcountryname = webCamModel!!.result?.webcams?.get(data)?.title
                        vedModel.playerlink =
                            webCamModel!!.result?.webcams?.get(data)?.player?.lifetime?.link
                        vedModel.imagelink =
                            webCamModel!!.result?.webcams?.get(data)?.image?.current?.thumbnail
                        list.add(vedModel)
                        data++
                    }
                    recyclerViewcon!!.layoutManager =
                        LinearLayoutManager(this@CountryVediosActivity)
                    vedAdapter = VedAdapter(this@CountryVediosActivity, list)
                    recyclerViewcon!!.adapter = vedAdapter
                    Log.d("52663", "response: $webCamModel")
                }
            }

            override fun onFailure(call: Call<WebCamModel?>, t: Throwable) {}
        })
    }
}