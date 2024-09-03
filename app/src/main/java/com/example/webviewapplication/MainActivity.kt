package com.example.webviewapplication

import Req
import Res
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.DrawableContainer.DrawableContainerState
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.webviewapplication.databinding.ActivityMainBinding
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.recaptcha.Recaptcha
import com.google.android.recaptcha.RecaptchaAction
import com.google.android.recaptcha.RecaptchaClient
import com.google.auth.oauth2.GoogleCredentials
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.io.InputStream
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var recaptchaClient: RecaptchaClient
    lateinit var binding: ActivityMainBinding
    private lateinit var biometricPrompt: BiometricPrompt

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        requestInAppReview()
//        initializeRecaptchaClient()
        CoroutineScope(Dispatchers.IO).launch {
            getAccessToken()
        }
        val apiService = RetrofitClient.instance

        binding.txtWeightValue.maxValue = 300
        binding.txtWeightValue.minValue = 10

        binding.txtWeightValue.apply {
            setOnValueChangedListener { numberPicker, i, i2 ->
                println(value)
            }
        }
        val executor = Executors.newSingleThreadExecutor()
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                // Handle authentication error
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                // Authentication succeeded, proceed with opening the app
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                // Handle authentication failure
            }
        }

        biometricPrompt = BiometricPrompt(this, executor, callback)
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Authenticate to open the app")
            .setSubtitle("Use fingerprint for authentication")
            .setNegativeButtonText("Cancel")
            .build()
        // Trigger biometric prompt when needed
//        biometricPrompt.authenticate(promptInfo)
        binding.btn.setOnClickListener {
            binding.progessBar.visibility = View.VISIBLE
//            executeLoginAction()
//            requestInAppReview()
            val call = apiService.getExampleData(
                Req(
                    apiKey = "121DDCD83E8A4FBA8F642DF36490383F",
                    userName = binding.mobileNum.text.toString(),
                    requestData = emptyList()
                )
            )

            call.enqueue(object : Callback<Res> {
                override fun onResponse(
                    call: Call<Res>,
                    response: Response<Res>
                ) {
                    if (response.isSuccessful) {
                        binding.progessBar.visibility = View.GONE
                        val exampleResponse = response.body()
                        if (exampleResponse?.responseCode == 404) toast(exampleResponse.msg.toString())
                        else
                            exampleResponse?.redirectUrl?.let { it1 ->
                                val intent = Intent(this@MainActivity, WebActivity::class.java)
                                intent.putExtra("URL", it1)
                                startActivity(intent)
                            }
                        // Handle successful response here
                    } else {
                        // Handle unsuccessful response here
                        binding.progessBar.visibility = View.GONE
                        toast(response.errorBody()?.string() ?: "Unknown error")
                    }
                }

                override fun onFailure(call: Call<Res>, t: Throwable) {
                    // Handle failure here
                }
            })
        }
    }

    private fun toast(response: String) {
        Toast.makeText(
            this@MainActivity,
            response,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun stateListDrawable(colorStroke: Int, bgColor: Int): StateListDrawable {
        val stateListDrawable = ContextCompat.getDrawable(
            this,
            R.drawable.vital_dotted_bg_pink
        ) as StateListDrawable
        // Access the single item in the StateListDrawable
        val drawableContainerState = stateListDrawable.constantState as DrawableContainerState
        val children = drawableContainerState.children

        if (children.isNotEmpty() && children[0] is GradientDrawable) {
            val shapeDrawable = children[0] as GradientDrawable
            // Modify the drawable programmatically
            shapeDrawable.color = ColorStateList.valueOf(getColor(R.color.colorVitalBeigeStroke))

            shapeDrawable.setStroke(
                1 * resources.displayMetrics.density.toInt(), // width in dp
                ContextCompat.getColor(this, R.color.colorVitalBeige), // new color
                5 * resources.displayMetrics.density, // new dash width in dp
                5 * resources.displayMetrics.density // new dash gap in dp
            )
        }
        return stateListDrawable
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

    private fun initializeRecaptchaClient() {
        lifecycleScope.launch {
            Recaptcha.getClient(application, "6LeMj9YpAAAAAOkdllliLGxEdWrsgr4p2DhteOs7")
                .onSuccess { client ->
                    showToast("initializeRecaptchaClient success")
                    recaptchaClient = client
                }
                .onFailure { exception ->
                    showToast(
                        "initializeRecaptchaClient failure exception: ${exception.message}",
                    )
                }
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
        println(msg)
    }

    private fun executeLoginAction() {
        lifecycleScope.launch {
            recaptchaClient
                .execute(RecaptchaAction.LOGIN)
                .onSuccess { token ->
                    if (token.isNotBlank()) {
                        showToast("Ready for login >> Token $token")
                    } else {
                        showToast("Not ready for login >> Token $token")
                    }
                }
                .onFailure { exception ->
                    showToast("executeLoginAction >> failure >> Exception :${exception.message}")
                }
        }
    }

    private suspend fun getAccessToken() {
        try {/*
            // Load the service account JSON key file
            val inputStream: InputStream = resources.openRawResource(R.raw.service_account)
            val googleCredentials = GoogleCredentials.fromStream(inputStream)
                .createScoped(listOf("https://www.googleapis.com/auth/firebase.messaging"))

            googleCredentials.refreshIfExpired()
            val accessToken = googleCredentials.accessToken.tokenValue

            Log.d("AccessToken", "Access Token: $accessToken")
            // Use the access token to make authenticated requests to FCM or other Google APIs
*/
        } catch (e: Exception) {
            Log.e("AccessToken", "Error obtaining access token", e)
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

fun Drawable.setStroke(colorStroke: Int, bgColor: Int, width: Int, view: View) {
    val dcs = view.background.constantState as DrawableContainerState?
    val drawableItems = dcs!!.children
    val gradientDrawableChecked = drawableItems[0] as GradientDrawable // item 1
    gradientDrawableChecked.setColor(bgColor)
    (this as? GradientDrawable)?.apply {
        mutate()
        gradientDrawableChecked.setStroke(width, colorStroke, 3f, 3f)
    }
}