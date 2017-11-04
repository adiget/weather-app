package com.ags.annada.weatherforecastapp;

import android.app.Application;
import android.content.SharedPreferences;

import com.ags.annada.weatherforecastapp.dagger2.ApplicationComponent;
import com.ags.annada.weatherforecastapp.dagger2.ApplicationModule;
import com.ags.annada.weatherforecastapp.dagger2.DaggerApplicationComponent;
import com.ags.annada.weatherforecastapp.utils.PreferenceUtils;

import javax.inject.Inject;

import static com.ags.annada.weatherforecastapp.globals.Constants.FIRST_LAUNCH;

/**
 * Created by annada on 18/10/2017.
 */

public class WeatherApplication extends Application {
    private boolean first_lunch_flag = true;
    private ApplicationComponent mApplicationComponent;

    @Inject
    SharedPreferences mSharedPrefs;

    @Override
    public void onCreate() {
        super.onCreate();

        getApplicationComponent().inject(this); // injection using Dagger 2

        first_lunch_flag = true;
        mSharedPrefs = PreferenceUtils.getPreferences(getApplicationContext());
    }

    public ApplicationComponent getApplicationComponent() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return mApplicationComponent;
    }

    public boolean isThisFirstTimeLaunch(){
        return first_lunch_flag && mSharedPrefs.getBoolean(FIRST_LAUNCH, true);
    }

    public void setFirstTimeLaunch(){
        first_lunch_flag = false;
        PreferenceUtils.save(mSharedPrefs,FIRST_LAUNCH, false);
    }
}
