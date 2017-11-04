package com.ags.annada.weatherforecastapp.utils;

/**
 * Created by annada on 19/10/2017.
 */

public class StringUtils {
    public static String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }
}
