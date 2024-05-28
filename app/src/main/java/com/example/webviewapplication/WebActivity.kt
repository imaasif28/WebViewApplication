package com.example.webviewapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.webviewapplication.databinding.ActivityWebBinding

class WebActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupWebView() {
        val webView = binding.webView
        webView.let {
            it.settings.javaScriptEnabled = true
            it.settings.domStorageEnabled = true
            it.settings.cacheMode = WebSettings.LOAD_NO_CACHE
            it.settings.loadsImagesAutomatically = true
            it.settings.javaScriptCanOpenWindowsAutomatically = true
            it.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
            it.settings.loadWithOverviewMode = true
            it.settings.allowFileAccess = true
            it.settings.mediaPlaybackRequiresUserGesture = false
            it.setLayerType(View.LAYER_TYPE_HARDWARE, null)
//            it.webChromeClient = ChromeClient()
        }
        webView.webViewClient = WebViewClient()
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                // Load URL within WebView
                view?.loadUrl(request?.url.toString())
                return true
            }
        }
    }

    override fun onStart() {
        super.onStart()
        setupWebView()
        val message = intent.getStringExtra("URL")
        if (message != null) {
            // Do something with the message
            binding.webView.loadUrl(message)
        } else {
            println("No URL received")
        }
    }
}