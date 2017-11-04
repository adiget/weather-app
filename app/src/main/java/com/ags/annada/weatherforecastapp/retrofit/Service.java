package com.ags.annada.weatherforecastapp.retrofit;

import com.ags.annada.weatherforecastapp.models.WeatherData;
import com.ags.annada.weatherforecastapp.models.WeatherForecast;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by : annada
 * Date : 27/10/2017.
 */

public class Service {
    private final NetworkService networkService;

    public Service(NetworkService networkService) {
        this.networkService = networkService;
    }

    public Subscription getWeatherByCity(final String city, final String lang, final GetWeatherByCityCallback callback) {

        return networkService.getWeatherByCity(city, lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends WeatherData>>() {
                    @Override
                    public Observable<? extends WeatherData> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<WeatherData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(WeatherData weatherDataResponse) {
                        callback.onSuccess(weatherDataResponse);
                    }
                });
    }

    public interface GetWeatherByCityCallback{
        void onSuccess(WeatherData weatherDataResponse);

        void onError(NetworkError networkError);
    }

    public Subscription getWeatherByLocation(final String latitude, final String longitude, final GetWeatherByLocationCallback callback) {

        return networkService.getWeatherByLocation(latitude, longitude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends WeatherData>>() {
                    @Override
                    public Observable<? extends WeatherData> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<WeatherData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(WeatherData weatherDataResponse) {
                        callback.onSuccess(weatherDataResponse);
                    }
                });
    }

    public interface GetWeatherByLocationCallback{
        void onSuccess(WeatherData weatherDataResponse);

        void onError(NetworkError networkError);
    }

    public Subscription getWeatherForecast(final String city, final String lang, final GetWeatherForecastCallback callback) {

        return networkService.getWeatherForecast(city, lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends WeatherForecast>>() {
                    @Override
                    public Observable<? extends WeatherForecast> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<WeatherForecast>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(WeatherForecast weatherForecastResponse) {
                        callback.onSuccess(weatherForecastResponse);

                    }
                });
    }

    public interface GetWeatherForecastCallback{
        void onSuccess(WeatherForecast weatherForecastResponse);

        void onError(NetworkError networkError);
    }

    public Subscription getWeatherForecast(final String city, final GetWeatherForecastCallback callback) {

        return networkService.getWeatherForecast(city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends WeatherForecast>>() {
                    @Override
                    public Observable<? extends WeatherForecast> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<WeatherForecast>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(WeatherForecast weatherForecastResponse) {
                        callback.onSuccess(weatherForecastResponse);

                    }
                });
    }
}
