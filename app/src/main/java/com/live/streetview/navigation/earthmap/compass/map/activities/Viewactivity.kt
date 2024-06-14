package com.live.streetview.navigation.earthmap.compass.map.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.live.streetview.navigation.earthmap.compass.map.R
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Viewactivity : AppCompatActivity() {
    private var webView: WebView? = null
    private var progressBar: ProgressBar? = null

    @SuppressLint("SetJavaScriptEnabled")
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewactivity)

        webView = findViewById(R.id.webView)
        progressBar = findViewById(R.id.progressbar)
        progressBar!!.max = 100

        val link = intent.getStringExtra("getplayerlink")

        webView?.settings?.javaScriptEnabled = true

        GlobalScope.launch(Dispatchers.Main) {
            loadUrl(link)
        }
    }

    private suspend fun loadUrl(url: String?) {
        if (url != null) {
            webView?.loadUrl(url)
        }
        webView?.webViewClient = object : WebViewClient() {
            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }
    }
}

