package com.ags.annada.weatherforecastapp.dagger2;

import com.ags.annada.weatherforecastapp.WeatherApplication;
import com.ags.annada.weatherforecastapp.retrofit.NetworkModule;
import com.ags.annada.weatherforecastapp.view.activities.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by annada on 18/10/2017.
 */

@ApplicationScope
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(WeatherApplication app);
}
