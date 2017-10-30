package com.example.annada.weatherforecastapp.model;

import com.example.annada.weatherforecastapp.MVP_Main;
import com.example.annada.weatherforecastapp.R;
import com.example.annada.weatherforecastapp.models.FiveWeathers;
import com.example.annada.weatherforecastapp.models.ForecastRequiredData;
import com.example.annada.weatherforecastapp.models.WeatherData;
import com.example.annada.weatherforecastapp.models.WeatherForecast;
import com.example.annada.weatherforecastapp.retrofit.NetworkError;
import com.example.annada.weatherforecastapp.retrofit.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by : annada
 * Date : 26/10/2017.
 */

public class MainModel implements MVP_Main.ProvidedModelOps {
    // Presenter reference
    private MVP_Main.RequiredPresenterOps mPresenter;
    // Recycler data
    public ArrayList<ForecastRequiredData> mForecastData;
    public WeatherData mWeatherData;
    private CompositeSubscription subscriptions;
    private Service service;

    /**
     * Main constructor, called by Activity during MVP setup
     * @param presenter Presenter instance
     */
    public MainModel(MVP_Main.RequiredPresenterOps presenter) {
        this.mPresenter = presenter;
        this.subscriptions = new CompositeSubscription();
        mForecastData = new ArrayList<ForecastRequiredData>();
    }

    /**
     * Main constructor, called by Activity during MVP setup
     * @param presenter Presenter instance
     */
    public MainModel(MVP_Main.RequiredPresenterOps presenter, Service service) {
        this.mPresenter = presenter;
        this.service = service;
        this.subscriptions = new CompositeSubscription();
        mForecastData = new ArrayList<ForecastRequiredData>();
    }

    /**
     * Called by Presenter when View is destroyed
     * @param isChangingConfiguration   true configuration is changing
     */
    @Override
    public void onDestroy(boolean isChangingConfiguration) {
        if (!isChangingConfiguration) {
            mPresenter = null;
            mForecastData = null;

            //Un-subscribe subscription
            rxUnSubscribe();
        }
    }

    public void rxUnSubscribe(){
        if(subscriptions!=null && !subscriptions.isUnsubscribed())
            subscriptions.unsubscribe();
    }

    @Override
    public boolean loadWeatherByCityData(String city, String lang) {
        Subscription subscription = service.getWeatherByCity(city, lang, new Service.GetWeatherByCityCallback() {
            @Override
            public void onSuccess(WeatherData weatherDataResponse) {
                mWeatherData = weatherDataResponse;
                mPresenter.notifyWeatherDataSuccess(weatherDataResponse);
            }

            @Override
            public void onError(NetworkError networkError) {
                mPresenter.notifyLoadDataError(networkError.getMessage());
            }

        });

        subscriptions.add(subscription);

        return mWeatherData != null;
    }

    @Override
    public boolean loadWeatherByLocationData(String latitude, String longitude) {

        Subscription subscription = service.getWeatherByLocation(latitude, longitude, new Service.GetWeatherByLocationCallback() {
            @Override
            public void onSuccess(WeatherData weatherDataResponse) {
                mWeatherData = weatherDataResponse;
                mPresenter.notifyWeatherDataSuccess(weatherDataResponse);
            }

            @Override
            public void onError(NetworkError networkError) {
                mPresenter.notifyLoadDataError(networkError.getMessage());
            }

        });

        subscriptions.add(subscription);

        return mWeatherData != null;
    }

    @Override
    public boolean loadWeatherForecastData(String city, String lang) {

        Subscription subscription = service.getWeatherForecast(city, lang, new Service.GetWeatherForecastCallback() {
            @Override
            public void onSuccess(WeatherForecast weatherForecastResponse) {
                makeForecastRequiredModel(weatherForecastResponse);
                mPresenter.notifyWeatherForecastSuccess(weatherForecastResponse);
            }

            @Override
            public void onError(NetworkError networkError) {
                mPresenter.notifyLoadDataError(networkError.getMessage());
            }

        });

        subscriptions.add(subscription);

        return mForecastData != null;
    }

    @Override
    public boolean loadWeatherForecastData(String city) {

        Subscription subscription = service.getWeatherForecast(city, new Service.GetWeatherForecastCallback() {
            @Override
            public void onSuccess(WeatherForecast weatherForecastResponse) {
                makeForecastRequiredModel(weatherForecastResponse);
                mPresenter.notifyWeatherForecastSuccess(weatherForecastResponse);
            }

            @Override
            public void onError(NetworkError networkError) {
                mPresenter.notifyLoadDataError(networkError.getMessage());
            }
        });

        subscriptions.add(subscription);

        return mForecastData != null;
    }

    private void makeForecastRequiredModel(WeatherForecast forecast) {
        mForecastData.clear();

        int[] everyday = new int[]{0, 0, 0, 0, 0, 0, 0};

        List<FiveWeathers> weatherInfo = forecast.getList();

        for (int i = 0; i < weatherInfo.size(); i++) {
            String time = weatherInfo.get(i).getDtTxt();
            String shortDay = convertTimeToDay(time);
            String temp = weatherInfo.get(i).getMain().getTemp();
            String tempMin = weatherInfo.get(i).getMain().getTempMin();

            if (convertTimeToDay(time).equals("Mon") && everyday[0] < 1) {
                mForecastData.add(new ForecastRequiredData(shortDay, R.drawable.small_weather_icon, temp, tempMin));
                everyday[0] = 1;
            }

            if (convertTimeToDay(time).equals("Tue") && everyday[1] < 1) {
                mForecastData.add(new ForecastRequiredData(shortDay, R.drawable.small_weather_icon, temp, tempMin));
                everyday[1] = 1;
            }

            if (convertTimeToDay(time).equals("Wed") && everyday[2] < 1) {
                mForecastData.add(new ForecastRequiredData(shortDay, R.drawable.small_weather_icon, temp, tempMin));
                everyday[2] = 1;
            }

            if (convertTimeToDay(time).equals("Thu") && everyday[3] < 1) {
                mForecastData.add(new ForecastRequiredData(shortDay, R.drawable.small_weather_icon, temp, tempMin));
                everyday[3] = 1;
            }

            if (convertTimeToDay(time).equals("Fri") && everyday[4] < 1) {
                mForecastData.add(new ForecastRequiredData(shortDay, R.drawable.small_weather_icon, temp, tempMin));
                everyday[4] = 1;
            }

            if (convertTimeToDay(time).equals("Sat") && everyday[5] < 1) {
                mForecastData.add(new ForecastRequiredData(shortDay, R.drawable.small_weather_icon, temp, tempMin));
                everyday[5] = 1;
            }

            if (convertTimeToDay(time).equals("Sun") && everyday[6] < 1) {
                mForecastData.add(new ForecastRequiredData(shortDay, R.drawable.small_weather_icon, temp, tempMin));
                everyday[6] = 1;
            }
        }
    }

    private String convertTimeToDay(String time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:SSSS", Locale.getDefault());
        String days = "";
        try {
            Date date = format.parse(time);
            System.out.println("Our time " + date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            days = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
            System.out.println("Our time " + days);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    /**
     * Get a specific forecast from list using its array position
     * @param position    Array position
     * @return            forecast from list
     */
    @Override
    public ForecastRequiredData getForecast(int position) {
        return mForecastData.get(position);
    }

    /**
     * Get ArrayList size
     * @return  ArrayList size
     */
    @Override
    public int getForecastCount() {
        if ( mForecastData != null )
            return mForecastData.size();
        return 0;
    }
}
