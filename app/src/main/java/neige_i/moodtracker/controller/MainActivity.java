package neige_i.moodtracker.controller;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.ImageView;

import neige_i.moodtracker.R;
import neige_i.moodtracker.model.Mood;
import neige_i.moodtracker.model.MoodPager;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private ImageView mSmileyImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Mood.initDrawables();
        Mood.initColours();

        mSmileyImage = new ImageView(this);
        mSmileyImage.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        mViewPager = (ViewPager) findViewById(R.id.main_activity_mood_pager);
        mViewPager.setAdapter(new MoodPager(mSmileyImage));
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setMood(position);
            }
        });
        mViewPager.setCurrentItem(Mood.DEFAULT_MOOD);
    }

    private void setMood(int position) {
        mViewPager.setBackgroundResource(Mood.MOOD_COLOURS[position]);
        mSmileyImage.setImageResource(Mood.MOOD_DRAWABLES[position]);
    }
}
