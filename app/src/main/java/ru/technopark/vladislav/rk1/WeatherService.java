package ru.technopark.vladislav.rk1;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

import java.io.IOException;

import ru.mail.weather.lib.City;
import ru.mail.weather.lib.Weather;
import ru.mail.weather.lib.WeatherStorage;
import ru.mail.weather.lib.WeatherUtils;

public class WeatherService extends IntentService {

    public final static String WEATHER_ACTION = "ru.technopark.rk1.action.weather";

    public WeatherService() {
        super("WeatherService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        WeatherStorage weatherStorage = WeatherStorage.getInstance(this);

        try {
            City city = weatherStorage.getCurrentCity();
            Weather weather = WeatherUtils.getInstance().loadWeather(city);
            weatherStorage.saveWeather(city, weather);

            Intent wIntent = new Intent(WEATHER_ACTION);
            sendBroadcast(wIntent);
        } catch (IOException e) {
            Toast.makeText(this, "There was an error during weather loading: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        if (intent.getBooleanExtra("once", false))
            stopSelf();
    }
}
