package neige_i.moodtracker.controller;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;
import neige_i.moodtracker.R;
import neige_i.moodtracker.model.Mood;
import neige_i.moodtracker.model.MoodPagerAdapter;
import neige_i.moodtracker.model.PrefResetReceiver;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private VerticalViewPager mMoodPager;
    private String mCommentary;
    private int mMood;
    private SharedPreferences mPreferences;
    private EditText mEditText;
    private boolean clearCommentaryPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        schedulePrefReset();

        mPreferences = getPreferences(MODE_PRIVATE);
        mMood = mPreferences.getInt("mood", Mood.DEFAULT_MOOD);
        mCommentary = mPreferences.getString("commentary", null);

        Mood.initDrawables();
        Mood.initColours();

        mMoodPager = findViewById(R.id.mood_pager);
        mMoodPager.setAdapter(new MoodPagerAdapter(getSupportFragmentManager()));
        mMoodPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mMoodPager.setBackgroundResource(Mood.MOOD_COLOURS[position]);
                clearCommentaryPref = mMoodPager.getCurrentItem() != mMood;
            }
        });
        mMoodPager.setCurrentItem(mMood);

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
                                if (mEditText.getText().length() > 0) {
                                    mCommentary = mEditText.getText().toString();
                                    mMood = mMoodPager.getCurrentItem();
                                    clearCommentaryPref = false;
                                }
                            }
                        })
                        .setNegativeButton(R.string.dialog_negative_btn, null)
                        .create();
                commentaryDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                commentaryDialog.show();

                mEditText = commentaryDialog.findViewById(R.id.commentary_input);
                if (mCommentary != null && !clearCommentaryPref) {
                    mEditText.setText(mCommentary);
                    mEditText.setSelection(mCommentary.length());
                }
                break;
            case R.id.history_ic:
                Toast.makeText(MainActivity.this, "Start activity", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPreferences.edit().putInt("mood", mMoodPager.getCurrentItem()).apply();
        mPreferences.edit().remove("commentary").apply();
//        Log.i("EditText content", mEditText.getText().length() + " -> " + mEditText.getText());
        if (mCommentary != null && !clearCommentaryPref)
            mPreferences.edit().putString("commentary", mCommentary).apply();
    }

    private void schedulePrefReset() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis()); // See if mandatory
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);

        PendingIntent broadcast = PendingIntent.getBroadcast(this, 0, new Intent(this, PrefResetReceiver.class), 0);

        ((AlarmManager) getSystemService(Context.ALARM_SERVICE)).setInexactRepeating(AlarmManager.RTC_WAKEUP,
                                                                                    calendar.getTimeInMillis(),
                                                                                    AlarmManager.INTERVAL_DAY,
                                                                                    broadcast);
    }
}
