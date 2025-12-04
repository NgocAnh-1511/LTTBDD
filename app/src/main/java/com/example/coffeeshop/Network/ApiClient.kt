package com.example.coffeeshop.Network

import android.content.Context
import android.content.SharedPreferences
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    private const val BASE_URL = "http://10.0.2.2:3000/api/" // Android emulator localhost
    // For real device, use your computer's IP: "http://192.168.x.x:3000/api/"
    
    init {
        android.util.Log.d("ApiClient", "API Base URL: $BASE_URL")
    }
    
    private var retrofit: Retrofit? = null
    private var apiService: ApiService? = null
    
    fun getApiService(context: Context): ApiService {
        if (apiService == null) {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                    val token = getToken(context)
                    if (token != null) {
                        request.addHeader("Authorization", "Bearer $token")
                    }
                    chain.proceed(request.build())
                }
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()
            
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            
            apiService = retrofit!!.create(ApiService::class.java)
        }
        return apiService!!
    }
    
    fun getToken(context: Context): String? {
        val prefs: SharedPreferences = context.getSharedPreferences("CoffeeShopPrefs", Context.MODE_PRIVATE)
        return prefs.getString("auth_token", null)
    }
    
    fun saveToken(context: Context, token: String) {
        val prefs: SharedPreferences = context.getSharedPreferences("CoffeeShopPrefs", Context.MODE_PRIVATE)
        prefs.edit().putString("auth_token", token).apply()
    }
    
    fun clearToken(context: Context) {
        val prefs: SharedPreferences = context.getSharedPreferences("CoffeeShopPrefs", Context.MODE_PRIVATE)
        prefs.edit().remove("auth_token").apply()
    }
    
    fun setBaseUrl(url: String) {
        // Reset retrofit instance when URL changes
        retrofit = null
        apiService = null
    }
}

