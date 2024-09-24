package com.example.webviewapplication

import Req
import Res
import VerTenantRes
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("Authentication/api/v2/Auth/verifyApiKey")
    fun getExampleData(@Body body: Req): Call<Res>

    @GET("Authentication/api/v2/Auth/VerifyTenant")
    fun verifyTenant(@Query("secretCode") secretCode: String): Call<VerTenantRes>
}

object RetrofitClient {

    private const val BASE_URL = "https://uat-api.livlonginsurance.com/"
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val client = OkHttpClient.Builder().also {
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
