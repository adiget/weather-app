package com.ags.annada.weatherforecastapp.view.activities;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ags.annada.weatherforecastapp.MVP_Main;
import com.ags.annada.weatherforecastapp.R;
import com.ags.annada.weatherforecastapp.StateMaintainer;
import com.ags.annada.weatherforecastapp.WeatherApplication;
import com.ags.annada.weatherforecastapp.dagger2.DaggerDependencies;
import com.ags.annada.weatherforecastapp.dagger2.Dependencies;
import com.ags.annada.weatherforecastapp.globals.Constants;
import com.ags.annada.weatherforecastapp.model.MainModel;
import com.ags.annada.weatherforecastapp.models.FiveWeathers;
import com.ags.annada.weatherforecastapp.models.ForecastRequiredData;
import com.ags.annada.weatherforecastapp.models.WeatherData;
import com.ags.annada.weatherforecastapp.models.WeatherForecast;
import com.ags.annada.weatherforecastapp.retrofit.NetworkModule;
import com.ags.annada.weatherforecastapp.presenter.MainPresenter;
import com.ags.annada.weatherforecastapp.utils.StringUtils;
import com.ags.annada.weatherforecastapp.view.adapters.RecyclerViewAdapter;
import com.github.pavlospt.CircleView;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LocationListener, MVP_Main.RequiredViewOps {
    private static final String TAG = SplashActivity.class.getSimpleName();

    Dependencies deps;

    @BindView(R.id.weather_daily_list) RecyclerView mRecyclerView;
    @BindView(R.id.city_country) TextView cityCountry;
    @BindView(R.id.current_date) TextView currentDate;
    @BindView(R.id.weather_icon) ImageView weatherImage;
    @BindView(R.id.weather_result) CircleView circleTitle;
    @BindView(R.id.wind_result) TextView windResult;
    @BindView(R.id.humidity_result) TextView humidityResult;
    @BindView(R.id.progressbar) ProgressBar mProgress;
    @BindView(R.id.weather_layout) LinearLayout mWeatherLayout;
    @BindView(R.id.swipe) SwipeRefreshLayout swipeView;

    private LocationManager locationManager;
    private Location location;
    private final int REQUEST_LOCATION = 200;
    private RecyclerViewAdapter mAdapter;
    private String city;
    private String lang;
    private MVP_Main.ProvidedPresenterOps mPresenter;

    @Inject
    public com.ags.annada.weatherforecastapp.retrofit.Service service;

    // Responsible to maintain the object's integrity
    // during configurations change
    private final StateMaintainer mStateMaintainer =
            new StateMaintainer( getFragmentManager(), MainActivity.class.getName());

    public Dependencies getDeps() {
        return deps;
    }

    public MVP_Main.ProvidedPresenterOps getPresenter(){
        return mPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        swipeView.setColorScheme(android.R.color.holo_blue_dark,
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_green_light);

        File cacheFile = new File(getCacheDir(), "responses");
        deps = DaggerDependencies.builder().networkModule(new NetworkModule(cacheFile)).build();

        getDeps().inject(this);

        city = Constants.DEFAULT_CITY;
        lang = Constants.DEFAULT_LANG;

        setupViews();

        setupMVP();

        locationManager = (LocationManager) getSystemService(android.app.Service.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }else{
            // make API call with longitude and latitude
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 2, this);

            if (locationManager != null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                mPresenter.getWeatherByLocation(Double.toString(location.getLatitude()), Double.toString(location.getLongitude()));
            }else{
                mPresenter.getWeatherByCity(city,lang);
            }
        }

        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeView.setRefreshing(true);
                Log.d("Swipe", "Refreshing Number");
                ( new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeView.setRefreshing(false);
                        mPresenter.getWeatherByLocation(Double.toString(location.getLatitude()), Double.toString(location.getLongitude()));
                    }
                }, 3000);
            }
        });
    }

    /**
     * Setup the Views
     */
    private void setupViews(){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new RecyclerViewAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mProgress = (ProgressBar) findViewById(R.id.progressbar);
    }

    /**
     * Setup Model View Presenter pattern.
     * Use a {@link StateMaintainer} to maintain the
     * Presenter and Model instances between configuration changes.
     * Could be done differently,
     * using a dependency injection for example.
     */
    private void setupMVP() {
        // Check if StateMaintainer has been created
        if (mStateMaintainer.firstTimeIn()) {
            // Create the Presenter
            MainPresenter presenter = new MainPresenter(this);

            // Create the Model
            MainModel model = new MainModel(presenter, service);

            // Set Presenter model
            presenter.setModel(model);

            // Add Presenter and Model to StateMaintainer
            mStateMaintainer.put(presenter);
            mStateMaintainer.put(model);

            // Set the Presenter as a interface
            // To limit the communication with it
            mPresenter = presenter;
        }
        // get the Presenter from StateMaintainer
        else {
            // Get the Presenter
            mPresenter = mStateMaintainer.get(MainPresenter.class.getName());

            // Updated the View in Presenter
            mPresenter.setView(this);
        }
    }

    @Override
    public void onDestroy() {
        mPresenter.onDestroy(isChangingConfigurations());

        super.onDestroy();
    }


    public void updateWeatherView(WeatherData weather){
        String city = weather.getName() + ", " + weather.getSys().getCountry();
        String todayDate = getTodayDateInStringFormat();
        Long tempVal = Math.round(Math.floor(Double.parseDouble(weather.getMain().getTemp())));
        String weatherTemp = String.valueOf(tempVal) + "°C";
        String weatherDescription = StringUtils.capitalizeFirstLetter(weather.getWeather().get(0).getDescription());
        String windSpeed = weather.getWind().getSpeed();
        String humidityValue = weather.getMain().getHumidity();

        mPresenter.getWeatherForecast(weather.getName());

        // populate View data
        Resources resources = getResources();
        int imageID = resources.getIdentifier(weather.getWeather().get(0).getMain().toLowerCase(), "drawable", getPackageName());

        if(imageID != 0  && Build.VERSION.SDK_INT >= 16) {
            mWeatherLayout.setBackground(resources.getDrawable(imageID));
        }

        cityCountry.setText(Html.fromHtml(city));
        currentDate.setText(Html.fromHtml(todayDate));
        circleTitle.setTitleText(Html.fromHtml(weatherTemp).toString());
        circleTitle.setSubtitleText(Html.fromHtml(weatherDescription).toString());
        windResult.setText(Html.fromHtml(windSpeed) + " km/h");
        humidityResult.setText(Html.fromHtml(humidityValue) + " %");

        Picasso.with(this)
                .load(Constants.ICON_URL + weather.getWeather().get(0).getIcon() + ".png")
                .fit()
                .into(weatherImage);
    }

    private String getTodayDateInStringFormat(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("E, d MMMM", Locale.getDefault());
        return df.format(c.getTime());
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

                        mPresenter.getWeatherByLocation(Double.toString(location.getLatitude()), Double.toString(location.getLongitude()));
                    }else{
                        mPresenter.getWeatherByCity(city,lang);
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

    @Override
    public Context getAppContext() {
        return getApplicationContext();
    }

    @Override
    public Context getActivityContext() {
        return this;
    }

    @Override
    public void showToast(Toast toast) {
        toast.show();
    }

    @Override
    public void showProgress() {
        mProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgress.setVisibility(View.GONE);
    }

    @Override
    public void showAlert(AlertDialog dialog) {
        dialog.show();
    }

    // Notify the RecyclerAdapter that a new item was inserted
    @Override
    public void notifyItemInserted(int adapterPos) {
        mAdapter.notifyItemInserted(adapterPos);
    }

    // notify the RecyclerAdapter that items has changed
    @Override
    public void notifyItemRangeChanged(int positionStart, int itemCount) {
        mAdapter.notifyItemRangeChanged(positionStart, itemCount);
    }

    @Override
    public void notifyWeatherDataSuccess(WeatherData weatherDataResponse) {
        updateWeatherView(weatherDataResponse);
    }

    @Override
    public void notifyWeatherForecastSuccess(WeatherForecast weatherForecastResponse) {
        //makeForecastRequiredModel(weatherForecastResponse);
        mAdapter.notifyDataSetChanged();
    }

    // notify the RecyclerAdapter that data set has changed
    @Override
    public void notifyDataSetChanged() {
        mAdapter.notifyDataSetChanged();
    }
}
