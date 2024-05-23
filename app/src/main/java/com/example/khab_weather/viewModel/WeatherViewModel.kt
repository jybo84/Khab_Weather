package com.example.weather_presentapp.viewModel

import androidx.lifecycle.ViewModel
import com.example.weather_presentapp.Server.ApiClient
import com.example.weather_presentapp.Server.ApiServices
import com.example.weather_presentapp.model.Repository.WeatherRepository

class WeatherViewModel(val repository: WeatherRepository): ViewModel(){

    constructor(): this(WeatherRepository(ApiClient().getClient().create(ApiServices::class.java))) //TODO что это

    fun loadCurrentWhether(lat: Double, lon: Double, units: String) =
        repository.getCurrentWeather(lat, lon, units)
}