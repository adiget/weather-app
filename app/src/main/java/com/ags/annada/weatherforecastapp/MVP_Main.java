package com.ags.annada.weatherforecastapp;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.ags.annada.weatherforecastapp.models.ForecastRequiredData;
import com.ags.annada.weatherforecastapp.models.WeatherData;
import com.ags.annada.weatherforecastapp.models.WeatherForecast;
import com.ags.annada.weatherforecastapp.view.adapters.ForecastViewHolder;

/**
 * Created by : annada
 * Date : 25/10/2017.
 */

public interface MVP_Main {
    /**
     * Required View methods available to Presenter.
     * A passive layer, responsible to show data
     * and receive user interactions
     */
    interface RequiredViewOps{
        // View operations permitted to Presenter
        Context getAppContext();

        Context getActivityContext();

        void showToast(Toast toast);

        void showProgress();

        void hideProgress();

        void showAlert(AlertDialog dialog);

        void notifyDataSetChanged();

        void notifyItemInserted(int layoutPosition);

        void notifyItemRangeChanged(int positionStart, int itemCount);

        void notifyWeatherDataSuccess(WeatherData weatherDataResponse);

        void notifyWeatherForecastSuccess(WeatherForecast weatherForecastResponse);
    }

    /**
     * Operations offered to View to communicate with Presenter.
     * Processes user interactions, sends data requests to Model, etc.
     */
    interface ProvidedPresenterOps{
        // Presenter operations permitted to View
        void onDestroy(boolean isChangingConfiguration);

        void setView(RequiredViewOps view);

        ForecastViewHolder createViewHolder(ViewGroup parent, int viewType);

        void bindViewHolder(ForecastViewHolder holder, int position);

        int getForecastCount();

        void getWeatherByCity(final String city, final String lang);

        void getWeatherByLocation(final String latitude, final String longitude);

        void getWeatherForecast(final String city, final String lang);

        void getWeatherForecast(final String city);
    }

    /**
     * Required Presenter methods available to Model.
     */
    interface RequiredPresenterOps{
        // Presenter operations permitted to Model
        Context getAppContext();

        Context getActivityContext();

        void notifyWeatherDataSuccess(WeatherData weatherDataResponse);

        void notifyWeatherForecastSuccess(WeatherForecast weatherForecastResponse);

        void notifyLoadDataError(String error);
    }

    /**
     * Operations offered to Model to communicate with Presenter
     * Handles all data business logic.
     */
    interface ProvidedModelOps{
        // Model operations permitted to Presenter
        void onDestroy(boolean isChangingConfiguration);

        boolean loadWeatherByCityData(final String city, final String lang);

        boolean loadWeatherByLocationData(final String latitude, final String longitude);

        boolean loadWeatherForecastData(final String city, final String lang);

        boolean loadWeatherForecastData(final String city);

        ForecastRequiredData getForecast(int position);

        int getForecastCount();
    }
}
