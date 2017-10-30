package com.example.annada.weatherforecastapp.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.annada.weatherforecastapp.MVP_Main;
import com.example.annada.weatherforecastapp.R;
import com.example.annada.weatherforecastapp.models.ForecastRequiredData;
import com.example.annada.weatherforecastapp.models.WeatherData;
import com.example.annada.weatherforecastapp.models.WeatherForecast;
import com.example.annada.weatherforecastapp.view.adapters.ForecastViewHolder;

import java.lang.ref.WeakReference;

/**
 * Created by : annada
 * Date : 26/10/2017.
 */

public class MainPresenter implements MVP_Main.ProvidedPresenterOps,MVP_Main.RequiredPresenterOps{
    // View reference. We use as a WeakReference
    // because the Activity could be destroyed at any time
    // and we don't want to create a memory leak
    private WeakReference<MVP_Main.RequiredViewOps> mView;

    // Model reference
    private MVP_Main.ProvidedModelOps mModel;

    /**
     * Presenter Constructor
     *
     * @param view MainActivity
     */
    public MainPresenter(MVP_Main.RequiredViewOps view) {
        mView = new WeakReference<>(view);
    }

    public void getWeatherByCity(final String city, final String lang) {
        getView().showProgress();

        mModel.loadWeatherByCityData(city, lang);
    }

    public void getWeatherByLocation(final String latitude, final String longitude) {
        getView().showProgress();

        mModel.loadWeatherByLocationData(latitude, longitude);
    }

    public void getWeatherForecast(final String city, final String lang) {
        getView().showProgress();

        mModel.loadWeatherForecastData(city, lang);
    }

    public void getWeatherForecast(final String city) {
        getView().showProgress();

        mModel.loadWeatherForecastData(city);
    }

    /**
     * Called by View every time it is destroyed.
     * @param isChangingConfiguration   true: is changing configuration
     *                                  and will be recreated
     */
    @Override
    public void onDestroy(boolean isChangingConfiguration) {
        // View show be null every time onDestroy is called
        mView = null;

        // Inform Model about the event
        mModel.onDestroy(isChangingConfiguration);

        // Activity destroyed
        if ( !isChangingConfiguration ) {
            // Nulls Model when the Activity destruction is permanent
            mModel = null;
        }
    }

    /**
     * Return the View reference.
     * Throw an exception if the View is unavailable.
     */
    private MVP_Main.RequiredViewOps getView() throws NullPointerException {
        if (mView != null)
            return mView.get();
        else
            throw new NullPointerException("View is unavailable");
    }

    /**
     * Called by View during the reconstruction events
     * @param view  Activity instance
     */
    @Override
    public void setView(MVP_Main.RequiredViewOps view) {
        mView = new WeakReference<>(view);
    }

    /**
     * Called by Activity during MVP setup. Only called once.
     * @param model Model instance
     */
    public void setModel(MVP_Main.ProvidedModelOps model) {
        mModel = model;

    }

    /**
     * Creat a Toast object with given message
     * @param msg   Toast message
     * @return      A Toast object
     */
    private Toast makeToast(String msg) {
        return Toast.makeText(getView().getAppContext(), msg, Toast.LENGTH_SHORT);
    }


    @Override
    public ForecastViewHolder createViewHolder(ViewGroup parent, int viewType) {
        ForecastViewHolder viewHolder;

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View viewTaskRow = inflater.inflate(R.layout.weather_daily_list, parent, false);
        viewHolder = new ForecastViewHolder(viewTaskRow);

        return viewHolder;
    }

    @Override
    public void bindViewHolder(ForecastViewHolder holder, int position) {
        final ForecastRequiredData forecast = mModel.getForecast(position);

        holder.dayOfWeek.setText(forecast.getDayOfWeek());
        holder.weatherIcon.setImageResource(forecast.getWeatherIcon());
        double mTemp = Double.parseDouble(forecast.getWeatherResult());
        holder.weatherResult.setText(String.valueOf(Math.round(mTemp)) + "°");
        holder.weatherResultSmall.setText(forecast.getWeatherResultSmall());
        holder.weatherResultSmall.setVisibility(View.GONE);
    }

    @Override
    public int getForecastCount() {
        return mModel.getForecastCount();
    }

    @Override
    public Context getAppContext() {
        try {
            return getView().getAppContext();
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public Context getActivityContext() {
        try {
            return getView().getActivityContext();
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public void notifyWeatherDataSuccess(WeatherData weatherDataResponse) {
        getView().hideProgress();

        getView().notifyWeatherDataSuccess(weatherDataResponse);

    }

    @Override
    public void notifyWeatherForecastSuccess(WeatherForecast weatherForecastResponse) {
        getView().hideProgress();

        getView().notifyWeatherForecastSuccess(weatherForecastResponse);
    }

    @Override
    public void notifyLoadDataError(String error) {
        getView().hideProgress();

        getView().showToast(makeToast("Error loading data." + error));
    }
}
