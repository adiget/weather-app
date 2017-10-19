package com.example.annada.weatherforecastapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.annada.weatherforecastapp.R;
import com.example.annada.weatherforecastapp.model.ForecastRequiredData;

import java.util.List;


/**
 * Created by annada on 19/10/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {
    private static final String TAG = RecyclerViewAdapter.class.getSimpleName();

    private Context mContext;
    private List<ForecastRequiredData> dailyWeather;

    public RecyclerViewAdapter(Context context, List<ForecastRequiredData> dailyWeather) {
        this.dailyWeather = dailyWeather;
        this.mContext = context;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_daily_list, parent, false);

        return new RecyclerViewHolders(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders viewHolder, final int position) {
        if(!dailyWeather.isEmpty()) {
            viewHolder.dayOfWeek.setText(dailyWeather.get(position).getDayOfWeek());
            viewHolder.weatherIcon.setImageResource(dailyWeather.get(position).getWeatherIcon());
            double mTemp = Double.parseDouble(dailyWeather.get(position).getWeatherResult());
            viewHolder.weatherResult.setText(String.valueOf(Math.round(mTemp)) + "°");
            viewHolder.weatherResultSmall.setText(dailyWeather.get(position).getWeatherResultSmall());
            viewHolder.weatherResultSmall.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if(dailyWeather != null){
            return dailyWeather.size();
        }else{
            return 0;
        }
    }
}
