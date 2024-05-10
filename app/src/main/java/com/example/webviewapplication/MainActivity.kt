package com.example.webviewapplication

import Req
import Res
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.example.webviewapplication.databinding.ActivityMainBinding
import com.google.android.play.core.review.ReviewManagerFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestInAppReview()
        val apiService = RetrofitClient.instance
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
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                // Load URL within WebView
                view?.loadUrl(request?.url.toString())
                return true
            }
        }
        binding.txtWeightValue.maxValue = 300
        binding.txtWeightValue.minValue = 10

        binding.txtWeightValue.apply {
            setOnValueChangedListener { numberPicker, i, i2 ->

                println(
                    value
                )

            }
        }


        binding.btn.setOnClickListener {
            requestInAppReview()

            val call = apiService.getExampleData(
                Req(
                    apiKey = "121DDCD83E8A4FBA8F642DF36490383F",
                    userName = binding.mobileNum.text.toString(),
                    requestData = emptyList()
                )
            )

           /* call.enqueue(object : Callback<Res> {
                override fun onResponse(
                    call: Call<Res>,
                    response: Response<Res>
                ) {
                    if (response.isSuccessful) {
                        val exampleResponse = response.body()

                        Toast.makeText(
                            this@MainActivity,
                            exampleResponse?.redirectUrl,
                            Toast.LENGTH_SHORT
                        ).show()

                        exampleResponse?.redirectUrl?.let { it1 -> binding.webView.loadUrl(it1) }
                        // Handle successful response here
                    } else {
                        // Handle unsuccessful response here

                        Toast.makeText(
                            this@MainActivity,
                            "fail",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }

                override fun onFailure(call: Call<Res>, t: Throwable) {
                    // Handle failure here
                }
            })*/
        }
    }
    private fun requestInAppReview() {
        val reviewManager = ReviewManagerFactory.create(this)
        val request = reviewManager.requestReviewFlow()

        request.addOnCompleteListener { requestInfo ->
            if (requestInfo.isSuccessful) {
                // The flow will be launched if the request is successful.
                val reviewInfo = requestInfo.result
                val flow = reviewManager.launchReviewFlow(this, reviewInfo)

                flow.addOnCompleteListener { _ ->
                    // The review flow has finished.
                    // You can log or perform actions after the review is completed.
                }
            } else {
                // There was a problem requesting the review flow.
                // You can log or handle the error accordingly.
            }
        }
    }

}

interface ApiService {

    @POST("Authentication.Web.Api/api/v2/Auth/verifyApiKey")
    fun getExampleData(@Body body: Req): Call<Res> // Replace ExampleResponse with your actual response model
}

object RetrofitClient {

    private const val BASE_URL = "https://uat-api.livlonginsurance.com/"
    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    val client = OkHttpClient.Builder().also {
        it.addInterceptor(logging) // For logging purposes
    }.build()
    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }
}