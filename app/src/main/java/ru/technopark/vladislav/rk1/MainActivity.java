package ru.technopark.vladislav.rk1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ru.mail.weather.lib.*;

public class MainActivity extends AppCompatActivity {

    public final static String WEATHER_ACTION = "ru.technopark.rk1.action.weather";

    WeatherStorage weatherStorage;

    Button buttonTown, buttonRefresh, buttonNoRefresh;
    TextView weatherText;

    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherStorage = WeatherStorage.getInstance(MainActivity.this);

        buttonTown = (Button)findViewById(R.id.button_town);
        buttonRefresh = (Button)findViewById(R.id.button_refresh);
        buttonNoRefresh = (Button)findViewById(R.id.button_norefresh);
        weatherText = (TextView)findViewById(R.id.weather_text);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                switch (v.getId()) {
                    case R.id.button_town:
                        intent = new Intent(MainActivity.this, TownActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.button_refresh:
                        intent = new Intent(MainActivity.this, WeatherService.class);
                        WeatherUtils.getInstance().schedule(MainActivity.this, intent);
                        break;
                    case R.id.button_norefresh:
                        intent = new Intent(MainActivity.this, WeatherService.class);
                        WeatherUtils.getInstance().unschedule(MainActivity.this, intent);
                        break;
                    default:
                        break;
                }
            }
        };

        buttonTown.setOnClickListener(listener);
        buttonRefresh.setOnClickListener(listener);
        buttonNoRefresh.setOnClickListener(listener);
    }

    @Override
    protected void onResume() {
        super.onStart();
        buttonTown.setText(weatherStorage.getCurrentCity().name());
        setWeather();

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                setWeather();
            }
        };
        IntentFilter intentFilter = new IntentFilter(WEATHER_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    void setWeather() {
        Weather weather = weatherStorage.getLastSavedWeather(weatherStorage.getCurrentCity());
        if (weather != null) {
            City city = weatherStorage.getCurrentCity();
            weatherText.setText(String.format("%s - %d (%s)", city.name(), weather.getTemperature(), weather.getDescription()));
        }
    }
}
