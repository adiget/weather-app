package com.example.annada.weatherforecastapp;

import com.example.annada.weatherforecastapp.globals.Constants;
import com.example.annada.weatherforecastapp.model.WeatherData;
import com.example.annada.weatherforecastapp.model.WeatherForecast;
import com.example.annada.weatherforecastapp.restapis.WeatherService;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static junit.framework.Assert.assertTrue;

/**
 * Created by annada on 19/10/2017.
 */

public class APITest {
    @Test
    public void TestRestApis() {
        final CountDownLatch signal = new CountDownLatch(1);
        final boolean[] statuses = {false, false};

        WeatherService.getAPI().getWeatherByCity(Constants.DEFAULT_CITY, Constants.DEFAULT_LANG,new Callback<WeatherData>() {
            @Override
            public void success(WeatherData weatherData, Response response) {
                statuses[0] = true;
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });

        WeatherService.getAPI().getWeatherForecast(Constants.DEFAULT_CITY, new Callback<WeatherForecast>() {
            @Override
            public void success(WeatherForecast weatherForecastData, Response response) {
                statuses[1] = true;
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });

        try {
            signal.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (boolean status : statuses)
            assertTrue(status);
    }
}
