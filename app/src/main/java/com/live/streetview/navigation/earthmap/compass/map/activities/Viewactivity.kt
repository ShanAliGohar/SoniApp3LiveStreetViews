package com.live.streetview.navigation.earthmap.compass.map.activities

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.live.streetview.navigation.earthmap.compass.map.R

class Viewactivity : AppCompatActivity() {
    private var webView: WebView? = null
    var progressBar: ProgressBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewactivity)
        webView = findViewById(R.id.webView)
        val webSettings = webView?.getSettings()
        val link = intent.getStringExtra("getplayerlink")
        webSettings?.javaScriptEnabled = true
        progressBar = findViewById<View>(R.id.progressbar) as ProgressBar
        progressBar!!.max = 100
        webSettings?.javaScriptEnabled = true
        webView?.loadUrl(link!!)
        webView?.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        })
    }
}
