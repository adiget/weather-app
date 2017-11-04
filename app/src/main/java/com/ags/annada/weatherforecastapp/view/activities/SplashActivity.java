package com.ags.annada.weatherforecastapp.view.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ags.annada.weatherforecastapp.R;
import com.ags.annada.weatherforecastapp.WeatherApplication;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by annada on 18/10/2017.
 */

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();

    private Timer mTimer = new Timer();
    private TimerTask mTimerTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                ((WeatherApplication)getApplication()).setFirstTimeLaunch();
                finish();
            }
        };

        mTimer.schedule(mTimerTask, getResources().getInteger(R.integer.splash_delay_millisecs));
    }
}
