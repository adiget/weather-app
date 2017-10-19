package com.example.annada.weatherforecastapp.dagger2;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;

import static com.example.annada.weatherforecastapp.globals.Constants.SHARED_PREF;

/**
 * Created by annada on 18/10/2017.
 */

@Module
public class ApplicationModule {
    private final Application mApplication;

    public ApplicationModule(Application application){
        mApplication = application;
    }

    @Provides
    @ApplicationScope
    Application provideApplicationContext() {
        return mApplication;
    }

    @Provides
    @ApplicationScope
    SharedPreferences provideSharedPreferences() {
        return mApplication.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
    }
}
