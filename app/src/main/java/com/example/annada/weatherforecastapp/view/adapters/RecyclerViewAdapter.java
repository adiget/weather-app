package com.example.annada.weatherforecastapp.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.annada.weatherforecastapp.R;
import com.example.annada.weatherforecastapp.models.ForecastRequiredData;
import com.example.annada.weatherforecastapp.view.activities.MainActivity;

import java.util.List;


/**
 * Created by annada on 19/10/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<ForecastViewHolder> {
    private static final String TAG = RecyclerViewAdapter.class.getSimpleName();

    private Context mContext;

    public RecyclerViewAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public ForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ((MainActivity)mContext).getPresenter().createViewHolder(parent,viewType);
    }

    @Override
    public void onBindViewHolder(ForecastViewHolder viewHolder, final int position) {
        ((MainActivity)mContext).getPresenter().bindViewHolder(viewHolder, position);
    }

    @Override
    public int getItemCount() {
        return ((MainActivity)mContext).getPresenter().getForecastCount();
    }
}
