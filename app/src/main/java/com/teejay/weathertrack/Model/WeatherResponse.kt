package com.teejay.weathertrack.Model

data class WeatherResponse(
    val address: String,
    val days: List<Day>
)

data class Day(
    val temp: Double,
    val humidity: Double,
    val conditions : String,
    val windspeed : Double,
    val precipprob : Double,
    val hours : List<Hour>
)

data class Hour(
    val datetime: String,
    val temp : Double,
    val conditions: String
)