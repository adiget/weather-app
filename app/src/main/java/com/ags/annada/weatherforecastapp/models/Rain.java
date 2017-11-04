package com.ags.annada.weatherforecastapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

/**
 * Created by annada on 18/10/2017.
 */

@Generated("org.jsonschema2pojo")
public class Rain {
    @SerializedName("3h")
    @Expose
    private Double _3h;

    public Double get3h() {
        return _3h;
    }

    public void set3h(Double _3h) {
        this._3h = _3h;
    }
}
