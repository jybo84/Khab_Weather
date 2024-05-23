package com.example.weather_presentapp.Server

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {
    private lateinit var retrofit: Retrofit

    private var client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    fun getClient(): Retrofit {
        var retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit
    }
}

//c216523d8aaccdb0f9e962cdda510c03