package com.example.weather_presentapp.model.Repository

import com.example.weather_presentapp.Server.ApiServices

class WeatherRepository(val api: ApiServices) {

    fun getCurrentWeather(lat: Double, lon: Double, units: String) =
        api.getCurrentWeather(lat,lon,units, "c216523d8aaccdb0f9e962cdda510c03")

}