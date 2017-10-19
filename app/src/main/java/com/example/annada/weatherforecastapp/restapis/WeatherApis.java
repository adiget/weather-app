package com.example.annada.weatherforecastapp.restapis;

import android.util.Log;

import com.example.annada.weatherforecastapp.globals.Constants;
import com.example.annada.weatherforecastapp.model.WeatherData;
import com.example.annada.weatherforecastapp.model.WeatherForecast;

import retrofit.Callback;
import retrofit.ErrorHandler;
import retrofit.Profiler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by annada on 18/10/2017.
 */

public interface WeatherApis {
    @GET(Constants.WEATHER_ENDPOINT + "&APPID=" + Constants.API_KEY + "&units=metric")
    void getWeatherByCity(@Query("q") String city, @Query("lang") String lang, Callback<WeatherData> response);

    @GET(Constants.WEATHER_ENDPOINT + "&APPID=" + Constants.API_KEY + "&units=metric")
    void getWeatherByLatLon(@Query("lat") String latitude, @Query("lon") String longitude, Callback<WeatherData> response);

    @GET(Constants.WEATHER_FORECAST_ENDPOINT + "&APPID=" + Constants.API_KEY + "&units=metric")
    void getWeatherForecast(@Query("q") String city, @Query("lang") String lang, Callback<WeatherForecast> response);

    @GET(Constants.WEATHER_FORECAST_ENDPOINT + "&APPID=" + Constants.API_KEY + "&units=metric")
    void getWeatherForecast(@Query("q") String city, Callback<WeatherForecast> response);
}
