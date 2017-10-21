package neige_i.moodtracker.controller;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import neige_i.moodtracker.R;
import neige_i.moodtracker.model.Mood;
import neige_i.moodtracker.model.MoodPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private ViewPager mMoodPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Mood.initDrawables();
        Mood.initColours();

        mMoodPager = (ViewPager) findViewById(R.id.mood_pager);
        mMoodPager.setAdapter(new MoodPagerAdapter(getSupportFragmentManager()));
        mMoodPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mMoodPager.setBackgroundResource(Mood.MOOD_COLOURS[position]);
            }
        });
        mMoodPager.setCurrentItem(Mood.DEFAULT_MOOD);
    }
}
