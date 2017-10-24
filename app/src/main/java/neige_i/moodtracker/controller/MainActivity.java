package neige_i.moodtracker.controller;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;
import neige_i.moodtracker.R;
import neige_i.moodtracker.model.Mood;
import neige_i.moodtracker.model.MoodPagerAdapter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private VerticalViewPager mMoodPager;
    private String mCommentary;
    private SharedPreferences mPreferences;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPreferences = getPreferences(MODE_PRIVATE);
        mCommentary = mPreferences.getString("commentary", null);

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
        mMoodPager.setCurrentItem(mPreferences.getInt("mood", Mood.DEFAULT_MOOD));

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
                                if (mEditText.getText().length() > 0)
                                    mCommentary = mEditText.getText().toString();
                            }
                        })
                        .setNegativeButton(R.string.dialog_negative_btn, null)
                        .create();
                commentaryDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                commentaryDialog.show();

                mEditText = commentaryDialog.findViewById(R.id.commentary_input);
                if (mCommentary != null) {
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
        if (mCommentary != null)
            mPreferences.edit().putString("commentary", mCommentary).apply();
    }
}
