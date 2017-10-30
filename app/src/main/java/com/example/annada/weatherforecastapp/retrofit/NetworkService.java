package com.example.annada.weatherforecastapp.retrofit;

import com.example.annada.weatherforecastapp.globals.Constants;
import com.example.annada.weatherforecastapp.models.WeatherData;
import com.example.annada.weatherforecastapp.models.WeatherForecast;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by : annada
 * Date : 27/10/2017.
 */

public interface NetworkService {
    @GET(Constants.WEATHER_ENDPOINT + "&APPID=" + Constants.API_KEY + "&units=metric")
    Observable<WeatherData> getWeatherByCity(@Query("q") String city, @Query("lang") String lang);

    @GET(Constants.WEATHER_ENDPOINT + "&APPID=" + Constants.API_KEY + "&units=metric")
    Observable<WeatherData> getWeatherByLocation(@Query("lat") String latitude, @Query("lon") String longitude);

    @GET(Constants.WEATHER_FORECAST_ENDPOINT + "&APPID=" + Constants.API_KEY + "&units=metric")
    Observable<WeatherForecast> getWeatherForecast(@Query("q") String city, @Query("lang") String lang);

    @GET(Constants.WEATHER_FORECAST_ENDPOINT + "&APPID=" + Constants.API_KEY + "&units=metric")
    Observable<WeatherForecast> getWeatherForecast(@Query("q") String city);
}
