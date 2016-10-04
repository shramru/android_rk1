package ru.technopark.vladislav.rk1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import ru.mail.weather.lib.City;
import ru.mail.weather.lib.WeatherStorage;

public class TownActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_town);

        LinearLayout ll = (LinearLayout)findViewById(R.id.town_layout);
        for (final City c : City.values()) {
            Button b = new Button(this);
            b.setText(c.name());
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button b = (Button) v;
                    WeatherStorage.getInstance(TownActivity.this).setCurrentCity(c);
                    finish();
                }
            });
            ll.addView(b);
        }
    }

}
