package com.ags.annada.weatherforecastapp.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ags.annada.weatherforecastapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by annada on 19/10/2017.
 */

public class ForecastViewHolder extends RecyclerView.ViewHolder{
    private static final String TAG = ForecastViewHolder.class.getSimpleName();

    @BindView(R.id.day_of_week) public TextView dayOfWeek;
    @BindView(R.id.weather_icon) public ImageView weatherIcon;
    @BindView(R.id.weather_result) public TextView weatherResult;
    @BindView(R.id.weather_result_small) public TextView weatherResultSmall;

    View itemView;

    public ForecastViewHolder(final View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}

