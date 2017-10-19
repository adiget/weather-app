package com.example.annada.weatherforecastapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.annada.weatherforecastapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by annada on 19/10/2017.
 */

public class RecyclerViewHolders extends RecyclerView.ViewHolder{
    private static final String TAG = RecyclerViewHolders.class.getSimpleName();

    @BindView(R.id.day_of_week) TextView dayOfWeek;
    @BindView(R.id.weather_icon) ImageView weatherIcon;
    @BindView(R.id.weather_result) TextView weatherResult;
    @BindView(R.id.weather_result_small) TextView weatherResultSmall;

    public RecyclerViewHolders(final View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}

