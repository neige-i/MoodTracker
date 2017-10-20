package neige_i.moodtracker.controller;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import neige_i.moodtracker.R;
import neige_i.moodtracker.model.MoodPager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.main_activity_mood_pager);
        viewPager.setAdapter(new MoodPager(this));
    }
}
