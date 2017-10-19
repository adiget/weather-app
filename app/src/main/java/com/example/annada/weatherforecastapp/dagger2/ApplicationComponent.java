package com.example.annada.weatherforecastapp.dagger2;

import com.example.annada.weatherforecastapp.WeatherApplication;

import dagger.Component;

/**
 * Created by annada on 18/10/2017.
 */

@ApplicationScope
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(WeatherApplication app);
}
