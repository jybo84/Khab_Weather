package com.example.khab_weather.Activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.khab_weather.R
import com.example.khab_weather.databinding.ActivityMainBinding
import com.example.weather_presentapp.model.CurrentResponseApi
import com.example.weather_presentapp.viewModel.WeatherViewModel
import com.github.matteobattilana.weather.PrecipType
import retrofit2.Call
import retrofit2.Response
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val weatherViewModel: WeatherViewModel by viewModels()
    val calendar by lazy { Calendar.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        window.apply {                                                //TODO window очень интересная штука - разобраться
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.TRANSPARENT
        }

        binding.apply {
            var lat = 48.4827
            var lon = 135.084

            weatherViewModel.loadCurrentWhether(lat, lon, "metric").enqueue(object :
                retrofit2.Callback<CurrentResponseApi> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    call: Call<CurrentResponseApi>,
                    response: Response<CurrentResponseApi>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        big.visibility = View.VISIBLE
                        data?.let {
                            weather.text =
                                it.weather?.get(0)?.main ?: "---"  //TODO здесь надо разобраться

                            speedWind.text = it.wind?.speed?.let { Math.round(it).toString() }

                            humidity.text = it.main?.humidity.toString()

                            temp.text = it.main?.temp?.let { Math.round(it).toString() }.toString()

                            tempMax.text = it.main?.tempMax?.let { Math.round(it).toString() }.toString()

                            tempMin.text = it.main?.tempMin?.let { Math.round(it).toString() }.toString()

                            cityName.text = it.name

                            val drawable = if (isNightNow()) R.drawable.night_bg
                            else {
                                setDynamicallyWallPaper(it.weather?.get(0)?.icon ?: "-")
                            }
                            back.setImageResource(drawable)

                            setEffectRainShow(it.weather?.get(0)?.icon ?: "---")

                        }
                    }
                }

                override fun onFailure(call: Call<CurrentResponseApi>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.toString(), Toast.LENGTH_SHORT).show()
                }

            })

        }
    }

        fun isNightNow(): Boolean {
            return calendar.get(Calendar.HOUR_OF_DAY) >= 18
        }

        fun setDynamicallyWallPaper(icon: String): Int {  // TODO разобраться 39-я минута
            return when (icon.dropLast(1)) {
                "01" -> {
                    initWeatherView(PrecipType.CLEAR)
                    R.drawable.cloudy_bg
                }

                "02", "03", "04" -> {
                    initWeatherView(PrecipType.CLEAR)
                    R.drawable.rainy_bg
                }

                "09", "10", "11" -> {
                    initWeatherView(PrecipType.CLEAR)
                    R.drawable.snow_bg
                }

                "50" -> {
                    initWeatherView(PrecipType.CLEAR)
                    R.drawable.haze_bg
                }

                else -> 0
            }
        }


    private fun setEffectRainShow(icon: String) {  // TODO разобраться 45-я минута
        when (icon.dropLast(1)) {
            "01" -> {
                initWeatherView(PrecipType.CLEAR)
            }

            "02", "03", "04" -> {
                initWeatherView(PrecipType.CLEAR)
            }

            "09", "10", "11" -> {
                initWeatherView(PrecipType.CLEAR)
            }

            "50" -> {
                initWeatherView(PrecipType.CLEAR)
            }
        }
    }

    private fun initWeatherView(type: PrecipType) {
        binding.weatherView.apply {
            setWeatherData(type)
            angle = -20
            emissionRate = 100.0f
        }
    }
}
