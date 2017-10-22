package neige_i.moodtracker.controller;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;
import neige_i.moodtracker.R;
import neige_i.moodtracker.model.Mood;
import neige_i.moodtracker.model.MoodPagerAdapter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private VerticalViewPager mMoodPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Mood.initDrawables();
        Mood.initColours();

        mMoodPager = findViewById(R.id.mood_pager);
        mMoodPager.setAdapter(new MoodPagerAdapter(getSupportFragmentManager()));
        mMoodPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mMoodPager.setBackgroundResource(Mood.MOOD_COLOURS[position]);
            }
        });
        mMoodPager.setCurrentItem(Mood.DEFAULT_MOOD);

        findViewById(R.id.new_note_ic).setOnClickListener(this);
        findViewById(R.id.history_ic).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_note_ic:
                AlertDialog commentaryDialog = new AlertDialog.Builder(this)
                        .setView(R.layout.text_input_layout)
                        .setPositiveButton(R.string.dialog_positive_btn, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "Save commentary", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(R.string.dialog_negative_btn, null)
                        .create();
                commentaryDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                commentaryDialog.show();
                break;
            case R.id.history_ic:
                Toast.makeText(MainActivity.this, "Start activity", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
