package com.example.annada.weatherforecastapp.activities;

import android.Manifest;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.annada.weatherforecastapp.R;
import com.example.annada.weatherforecastapp.WeatherApplication;
import com.example.annada.weatherforecastapp.adapters.RecyclerViewAdapter;
import com.example.annada.weatherforecastapp.globals.Constants;
import com.example.annada.weatherforecastapp.model.FiveWeathers;
import com.example.annada.weatherforecastapp.model.ForecastRequiredData;
import com.example.annada.weatherforecastapp.model.WeatherData;
import com.example.annada.weatherforecastapp.model.WeatherForecast;
import com.example.annada.weatherforecastapp.restapis.WeatherService;
import com.example.annada.weatherforecastapp.utils.StringUtils;
import com.github.pavlospt.CircleView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private static final String TAG = SplashActivity.class.getSimpleName();

    @BindView(R.id.weather_daily_list) RecyclerView mRecyclerView;
    @BindView(R.id.city_country) TextView cityCountry;
    @BindView(R.id.current_date) TextView currentDate;
    @BindView(R.id.weather_icon) ImageView weatherImage;
    @BindView(R.id.weather_result) CircleView circleTitle;
    @BindView(R.id.wind_result) TextView windResult;
    @BindView(R.id.humidity_result) TextView humidityResult;

    private LocationManager locationManager;
    private Location location;
    private final int REQUEST_LOCATION = 200;
    private RecyclerViewAdapter mAdapter;
    private List<ForecastRequiredData> mData;
    private String city;
    private String lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        city = Constants.DEFAULT_CITY;
        lang = Constants.DEFAULT_LANG;

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mData = new ArrayList<ForecastRequiredData>();
        mAdapter = new RecyclerViewAdapter(this, mData);
        mRecyclerView.setAdapter(mAdapter);

        locationManager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }else{
            // make API call with longitude and latitude
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 2, this);

            if (locationManager != null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                WeatherService.getAPI().getWeatherByLatLon(Double.toString(location.getLatitude()),Double.toString(location.getLongitude()),new Callback<WeatherData>() {
                    @Override
                    public void success(WeatherData weatherData, Response response) {
                        updateWeatherView(weatherData);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(TAG, "getWeather failed");
                    }
                });
            }else{
                WeatherService.getAPI().getWeatherByCity(city, lang, new Callback<WeatherData>() {
                    @Override
                    public void success(WeatherData weatherData, Response response) {
                        updateWeatherView(weatherData);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(TAG, "getWeather failed");
                    }
                });
            }
        }
    }

    void getWeatherForecast(String city){
        WeatherService.getAPI().getWeatherForecast(city, lang, new Callback<WeatherForecast>() {
            @Override
            public void success(WeatherForecast weatherForecastData, Response response) {
                makeForecastRequiredModel(weatherForecastData);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                mData.clear();
                mAdapter.notifyDataSetChanged();

                Log.d(TAG, "getWeatherForecast failed");
            }
        });
    }

    void makeForecastRequiredModel(WeatherForecast forecast) {
        mData.clear();

        int[] everyday = new int[]{0, 0, 0, 0, 0, 0, 0};

        List<FiveWeathers> weatherInfo = forecast.getList();

        for (int i = 0; i < weatherInfo.size(); i++) {
            String time = weatherInfo.get(i).getDtTxt();
            String shortDay = convertTimeToDay(time);
            String temp = weatherInfo.get(i).getMain().getTemp();
            String tempMin = weatherInfo.get(i).getMain().getTempMin();

            if (convertTimeToDay(time).equals("Mon") && everyday[0] < 1) {
                mData.add(new ForecastRequiredData(shortDay, R.drawable.small_weather_icon, temp, tempMin));
                everyday[0] = 1;
            }

            if (convertTimeToDay(time).equals("Tue") && everyday[1] < 1) {
                mData.add(new ForecastRequiredData(shortDay, R.drawable.small_weather_icon, temp, tempMin));
                everyday[1] = 1;
            }

            if (convertTimeToDay(time).equals("Wed") && everyday[2] < 1) {
                mData.add(new ForecastRequiredData(shortDay, R.drawable.small_weather_icon, temp, tempMin));
                everyday[2] = 1;
            }

            if (convertTimeToDay(time).equals("Thu") && everyday[3] < 1) {
                mData.add(new ForecastRequiredData(shortDay, R.drawable.small_weather_icon, temp, tempMin));
                everyday[3] = 1;
            }

            if (convertTimeToDay(time).equals("Fri") && everyday[4] < 1) {
                mData.add(new ForecastRequiredData(shortDay, R.drawable.small_weather_icon, temp, tempMin));
                everyday[4] = 1;
            }

            if (convertTimeToDay(time).equals("Sat") && everyday[5] < 1) {
                mData.add(new ForecastRequiredData(shortDay, R.drawable.small_weather_icon, temp, tempMin));
                everyday[5] = 1;
            }

            if (convertTimeToDay(time).equals("Sun") && everyday[6] < 1) {
                mData.add(new ForecastRequiredData(shortDay, R.drawable.small_weather_icon, temp, tempMin));
                everyday[6] = 1;
            }
        }
    }

    public void updateWeatherView(WeatherData weather){
        String city = weather.getName() + ", " + weather.getSys().getCountry();
        String todayDate = getTodayDateInStringFormat();
        Long tempVal = Math.round(Math.floor(Double.parseDouble(weather.getMain().getTemp())));
        String weatherTemp = String.valueOf(tempVal) + "°";
        String weatherDescription = StringUtils.capitalizeFirstLetter(weather.getWeather().get(0).getDescription());
        String windSpeed = weather.getWind().getSpeed();
        String humidityValue = weather.getMain().getHumidity();

        getWeatherForecast(weather.getName());

        // populate View data
        cityCountry.setText(Html.fromHtml(city));
        currentDate.setText(Html.fromHtml(todayDate));
        circleTitle.setTitleText(Html.fromHtml(weatherTemp).toString());
        circleTitle.setSubtitleText(Html.fromHtml(weatherDescription).toString());
        windResult.setText(Html.fromHtml(windSpeed) + " km/h");
        humidityResult.setText(Html.fromHtml(humidityValue) + " %");
    }

    private String getTodayDateInStringFormat(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("E, d MMMM", Locale.getDefault());
        return df.format(c.getTime());
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

    @Override
    protected void onStart() {
        super.onStart();

        if(((WeatherApplication)getApplication()).isThisFirstTimeLaunch()){
            startActivity(new Intent(this, SplashActivity.class));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //make api call
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 2, this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        WeatherService.getAPI().getWeatherByLatLon(Double.toString(location.getLatitude()),Double.toString(location.getLongitude()),new Callback<WeatherData>() {
                            @Override
                            public void success(WeatherData weatherData, Response response) {
                                updateWeatherView(weatherData);
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.d(TAG, "getWeather failed");
                            }
                        });
                    }else{
                        WeatherService.getAPI().getWeatherByCity(city, lang, new Callback<WeatherData>() {
                            @Override
                            public void success(WeatherData weatherData, Response response) {
                                updateWeatherView(weatherData);
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.d(TAG, "getWeather failed");
                            }
                        });
                    }
                }
            }else{
                Toast.makeText(this, getString(R.string.permission_notice), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String provider) {
        if (provider.equals(LocationManager.GPS_PROVIDER)) {
            showGPSDisabledAlertToUser();
        }
    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(callGPSSettingIntent);
                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
}
