package com.teejay.weathertrack.Activity

import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.media.MediaPlayer
import android.widget.AutoCompleteTextView
import android.widget.Adapter
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.teejay.weathertrack.Network.RetrofitInstance
import com.teejay.weathertrack.Model.WeatherResponse
import androidx.recyclerview.widget.LinearLayoutManager
import com.teejay.weathertrack.Adapter.HourlyAdapter
import com.teejay.weathertrack.Adapter.OtherCityAdapter
import com.teejay.weathertrack.Model.CityModel
import com.teejay.weathertrack.Model.HourlyModel
import com.teejay.weathertrack.R
import com.teejay.weathertrack.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)


        setContentView(binding.root)

        val searchButton = findViewById<Button>(R.id.button)
        val cityEditText = findViewById<AutoCompleteTextView>(R.id.editTextText)
        val citySuggestions = listOf(
            "Lagos",
            "London",
            "Tokyo",
            "Paris",
            "New York ",
            "Berlin",
            "Dubai",
            "Toronto",
            "Sydney",
            "Cape Town"
        )
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line, citySuggestions
        )
        cityEditText.setAdapter(adapter)



        binding.chipNavigator.setItemSelected(R.id.home,true)


        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        //Objects running recycler
        initRecyclerOtherCity()


        //Search Button
        searchButton.setOnClickListener {
            val cityName = cityEditText.text.toString()

            if(cityName.isEmpty()){
                Toast.makeText(this, "Enter city name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            lifecycleScope.launch {
                val cities = listOf(
                    "Lagos",
                    "London",
                    "Tokyo",
                    "Paris"
                )
                val cityItems = ArrayList<CityModel>()
                for (city in cities) {
                    val response = RetrofitInstance.api.getWeather(
                        city,
                       apiKey = "A8SFGUU6ET4Z5NR4Q45CGZRV2"
                    )


                    binding.view2.layoutManager = LinearLayoutManager(
                        this@MainActivity,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                    binding.view2.adapter = OtherCityAdapter(cityItems)



                    if (response.isSuccessful && response.body() != null){
                        val weatherResponse =response.body()!!
                        val todayWeather = weatherResponse.days[0]

                        val picPath = when {
                            todayWeather.conditions.contains("Rain", true) -> "rainy"
                            todayWeather.conditions.contains("Cloud", true) -> "cloudy"
                            todayWeather.conditions.contains("Snow", true) -> "snowy"
                            todayWeather.conditions.contains("Clear", true) -> "sunny"

                            else -> "sunrise"
                        }
                        cityItems.add(
                            CityModel(
                                cityName = weatherResponse.address,
                                temp = todayWeather.temp.toInt(),
                                picPath = picPath,
                                wind = todayWeather.windspeed.toInt(),
                                humidity = todayWeather.humidity.toInt(),
                                rain = todayWeather.precipprob.toInt()
                            )
                        )
                    }
                }


                try {
                    val response = RetrofitInstance.api.getWeather(
                        city = cityName,
                        apiKey = "A8SFGUU6ET4Z5NR4Q45CGZRV2"
                    )
                    if (response.isSuccessful && response.body() != null) {
                        val weatherResponse = response.body()!!
                        val todayWeather = weatherResponse.days[0]
                        val temperature = todayWeather.temp
                        val humidity = todayWeather.humidity
                        val hourlyItems = ArrayList<HourlyModel>()
                        for (hour in todayWeather.hours) {
                            val icon = when {
                                hour.conditions.contains("Rain", true) -> "rainy"
                                hour.conditions.contains("Snow", true) -> "snowy"
                                hour.conditions.contains("Cloud", true) -> "cloudy"
                                hour.conditions.contains("Clear", true) -> "sunny"

                                else -> "sunrise"
                            }
                            hourlyItems.add(
                                HourlyModel(
                                    hour.datetime,
                                    hour.temp.toInt(),
                                    icon
                                )
                            )
                        }
                        initRecyclerviewHourly(hourlyItems)
                        val conditions = todayWeather.conditions


                        val videoResId = when {
                            conditions.contains("Rain", true) ->R.raw.rainy
                            conditions.contains("Clear", true) ->R.raw.sunny
                            conditions.contains("Cloud", true) ->R.raw.cloudy
                            else -> R.raw.night
                        }
                        val videoPath = "android.resource://" + packageName + "/" + videoResId
                        binding.videoBackground.setVideoPath(
                            videoPath
                        )
                        binding.videoBackground.start()
                        binding.videoBackground.setOnPreparedListener{
                            it.isLooping = true
                            it.setVolume(0f, 0f)
                        }



                        when {
                            conditions.contains("Rain", true) -> {
                                binding.imageView.setImageResource(R.drawable.rainy)
                            }

                            conditions.contains("Clear", true) -> {
                                binding.imageView.setImageResource(R.drawable.sunny)
                            }

                            conditions.contains("Cloud", true) -> {
                                binding.imageView.setImageResource(R.drawable.cloudy)
                            }

                            conditions.contains("Snow", true) -> {
                                binding.imageView.setImageResource(R.drawable.snowy)
                            }

                            else -> {
                                binding.imageView.setImageResource(R.drawable.sunrise)
                            }
                        }
                        val windSpeed = todayWeather.windspeed
                        val rainChance= todayWeather.precipprob

                        binding.temperature.text = "${temperature.toInt()}C"
                        binding.windspeedNo.text = "${windSpeed.toInt()} km/h"
                        binding.humidityNo.text = "${humidity.toInt()}%"
                        binding.rainNo.text = "${rainChance.toInt()}%"

                        val cityItems = ArrayList<CityModel>()
                        val picPath = when {
                            todayWeather.conditions.contains("Rain", true) -> "rainy"
                            todayWeather.conditions.contains("Cloud", true) -> "cloudy"
                            todayWeather.conditions.contains("Snow", true) -> "snowy"
                            todayWeather.conditions.contains("Clear", true) -> "sunny"

                            else -> "sunrise"


                        }
                        cityItems.add(
                            CityModel(
                                cityName = weatherResponse.address,
                                temp = todayWeather.temp.toInt(),
                                picPath = picPath,
                                wind = todayWeather.windspeed.toInt(),
                                humidity = todayWeather.humidity.toInt(),
                                rain = todayWeather.precipprob.toInt()
                            )
                        )

                        binding.view2.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)

                    }
                }catch (e: Exception){
                    e.printStackTrace()
                }

            }
        }

    }

    // Recycler view other city display
    private fun initRecyclerOtherCity() {
        val items:ArrayList<CityModel> = ArrayList()

        binding.view2.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.view2.adapter=OtherCityAdapter(items)

    }

    //Recycler view hourly display
    private fun initRecyclerviewHourly(items:ArrayList<HourlyModel>) {
        binding.view1.setLayoutManager(
            LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false))
        binding.view1.adapter=HourlyAdapter(items)
    }
}