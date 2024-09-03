package com.example.webviewapplication

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AlertDialog
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
            it.webViewClient = webViewClient
//            it.webChromeClient = ChromeClient()
        }
    }

    private val webViewClient = object : WebViewClient() {
        @SuppressLint("WebViewClientOnReceivedSslError")
        override fun onReceivedSslError(
            view: WebView, handler: SslErrorHandler, error: SslError,
        ) {
            handler.proceed()
//            val builder = AlertDialog.Builder(this@WebActivity.baseContext, R.style.AlertDialogTheme)
//            val alertDialog: AlertDialog = builder.create()
//            var message = "SSL Certificate error."
//            when (error.primaryError) {
//                SslError.SSL_UNTRUSTED -> message = "The certificate authority is not trusted."
//                SslError.SSL_EXPIRED -> message = "The certificate has expired."
//                SslError.SSL_IDMISMATCH -> message = "The certificate Hostname mismatch."
//                SslError.SSL_NOTYETVALID -> message = "The certificate is not yet valid."
//            }
//            message += " Do you want to continue anyway?"
//            alertDialog.setTitle("SSL Certificate Error")
//            alertDialog.setMessage(message)
//            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK") { _, _ ->
//                handler.proceed()
//            }
//            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel")
//            { _, _ -> handler.cancel() }
//            alertDialog.show()
        }

        override fun shouldOverrideUrlLoading(
            view: WebView?, request: WebResourceRequest,
        ): Boolean {
            println("New shouldOverrideUrlLoading********************************${request.url}")
            view?.loadUrl(request.url.toString())
            return handleUri(request.url)
        }

        @SuppressWarnings("deprecation")
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            println("Old shouldOverrideUrlLoading********************************$url")
                    view.loadUrl(url)
            return handleUri(url)
        }

        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            binding.progessBar.visibility = View.VISIBLE
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
//            injectJavaScripHideSomeView(view)
            binding.progessBar.visibility = View.GONE
        }
    }


    private fun handleUri(uri: Any): Boolean {
        /*  if (uri.toString().contains("http") || uri.toString().contains("https")) {
              val intent = Intent(Intent.ACTION_VIEW)
              intent.data = Uri.parse(uri.toString())
              val title = "Choose Your Browser"
              val chooser = Intent.createChooser(intent, title)
              startActivity(chooser)
              return true
          }*/

        return true
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