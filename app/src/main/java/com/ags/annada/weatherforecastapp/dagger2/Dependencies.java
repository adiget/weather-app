package com.ags.annada.weatherforecastapp.dagger2;

import com.ags.annada.weatherforecastapp.retrofit.NetworkModule;
import com.ags.annada.weatherforecastapp.view.activities.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by : annada
 * Date : 28/10/2017.
 */

@Singleton
@Component(modules = {NetworkModule.class,})
public interface Dependencies {
    void inject(MainActivity mainActivity);
}

