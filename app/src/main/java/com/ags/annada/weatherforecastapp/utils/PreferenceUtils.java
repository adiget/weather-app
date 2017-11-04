package com.ags.annada.weatherforecastapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by annada on 18/10/2017.
 */

public class PreferenceUtils {
    public static void save(SharedPreferences preferences, String key, Object newValue) {
        SharedPreferences.Editor editor = preferences.edit();
        save(editor,key,newValue);
        editor.apply();
    }

    public static SharedPreferences getPreferences(Context context){
        return context.getApplicationContext().
                getSharedPreferences(context.getPackageName(),MODE_PRIVATE);
    }

    private static void save(SharedPreferences.Editor editor,String key, Object newValue){
        if(newValue instanceof Boolean)
            editor.putBoolean(key,(Boolean) ((Boolean) newValue));

        if(newValue instanceof String)
            editor.putString(key,(String)newValue);
    }
}
