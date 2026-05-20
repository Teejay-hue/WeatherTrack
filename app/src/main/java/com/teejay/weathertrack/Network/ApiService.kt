package com.teejay.weathertrack.Network

import com.teejay.weathertrack.Model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("VisualCrossingWebServices/rest/services/timeline/{city}")

    suspend fun getWeather(

        @Path("city") city: String,
        @Query("unitGroup") unitGroup: String = "metric",
        @Query("key") apiKey: String,
        @Query("contentType") contentType: String = "json"

    ): Response<WeatherResponse>
}