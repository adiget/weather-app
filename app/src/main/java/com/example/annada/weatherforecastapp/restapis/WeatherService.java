package com.example.annada.weatherforecastapp.restapis;

import android.util.Log;

import com.example.annada.weatherforecastapp.globals.Constants;

import retrofit.ErrorHandler;
import retrofit.Profiler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

/**
 * Created by annada on 18/10/2017.
 */

public class WeatherService {
    private static WeatherApis mWebService;

    public static synchronized WeatherApis getAPI(){

        if(mWebService == null){
            RequestInterceptor requestInterceptor = new RequestInterceptor() {
                @Override
                public void intercept(RequestInterceptor.RequestFacade request) {
                    request.addHeader("Accept", "application/json");
                }
            };

            RestAdapter restAdapter = new RestAdapter
                    .Builder()
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setEndpoint(Constants.BASE_URL)
                    .setRequestInterceptor(requestInterceptor)
                    .setProfiler(new Profiler() {
                        @Override
                        public Object beforeCall() {
                            return null;
                        }

                        @Override
                        public void afterCall(RequestInformation requestInfo, long elapsedTime, int statusCode, Object beforeCallData) {
                            Log.d("Retrofit Profiler", String.format("HTTP %d %s %s (%dms)",
                                    statusCode, requestInfo.getMethod(), requestInfo.getRelativePath(), elapsedTime));
                        }
                    })
                    .setErrorHandler(new ErrorHandler() {
                        @Override
                        public Throwable handleError(RetrofitError cause) {
                            return cause;
                        }
                    })
                    .build();

            mWebService = restAdapter.create(WeatherApis.class);
        }

        return mWebService;
    }
}
