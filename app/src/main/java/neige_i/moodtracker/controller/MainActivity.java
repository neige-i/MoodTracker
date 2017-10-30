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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;
import neige_i.moodtracker.R;
import neige_i.moodtracker.model.Mood;
import neige_i.moodtracker.model.MoodPagerAdapter;
import neige_i.moodtracker.model.PrefUpdateReceiver;
import neige_i.moodtracker.model.Storage;

import static neige_i.moodtracker.model.Mood.MOOD_COUNT;
import static neige_i.moodtracker.model.Mood.MOOD_DEFAULT;
import static neige_i.moodtracker.model.Mood.MOOD_EMPTY;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    // -------------------------------------     INSTANCE VARIABLES     -------------------------------------

    /**
     * Vertical ViewPager that allows the user to swipe between the different smileys.
     */
    private VerticalViewPager mMoodPager;
    /**
     * EditText, displayed in the dialog, that allows the user to put a commentary.
     */
    private EditText mCommentaryInput;
    /**
     * SharedPreferences that allows data saving/loading.
     */
    private SharedPreferences mPreferences;
    /**
     * Commentary of the current day.
     */
    private String mCommentary;
    /**
     * Smiley of the current day.
     */
    private int mSmiley;
    /**
     * Control variable to determinate actions to do regarding the commentary. It is useful in 2 cases.
     * <p>The first case is at initializing the dialog's EditText.<br />
     * If the user already entered a commentary for a specified mood, he can modify it by displaying the dialog again.
     * Meanwhile, if the user changes the mood, it would be inappropriate if he can still see his commentary.
     * In this situation, the EditText must be empty.</p>
     * <p>The second case is at saving the preferences.<br />
     * If the user already entered a commentary for the current day and then changes the mood without putting a new one,
     * the old commentary must be cleared from the preferences.</p>
     * @see #mCommentaryInput
     */
    private boolean clearCommentaryPref;
    /**
     * Constant for the smiley key in the preferences.
     */
    private final String PREF_KEY_SMILEY = "PREF_KEY_SMILEY";
    /**
     * Constant for the commentary key in the preferences.
     */
    private final String PREF_KEY_COMMENTARY = "PREF_KEY_COMMENTARY";
    private final String PREF_KEY_MOOD_ = "PREF_KEY_MOOD_";
    private Storage mStorage;

    // ---------------------------------------     CLASS VARIABLES     --------------------------------------

    /**
     * Array containing the drawables of the different smileys.<br />
     * Each drawable has a specific background color.
     * @see #MOOD_COLOURS
     */
    public static final int[] MOOD_DRAWABLES = new int[MOOD_COUNT];
    /**
     * Array containing the colors of the different backgrounds.<br />
     * Each color is the background for a specific drawable.
     * @see #MOOD_DRAWABLES
     */
    public static final int[] MOOD_COLOURS = new int[MOOD_COUNT];

    // -------------------------------------     OVERRIDDEN METHODS     -------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        schedulePrefUpdate();

        initMoodFromPrefs();

        initDrawables();
        initColours();

        initMoodPager();
        findViewById(R.id.new_note_ic).setOnClickListener(this);
        findViewById(R.id.history_ic).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_note_ic:
                AlertDialog commentaryDialog = new AlertDialog.Builder(this)
                        .setView(R.layout.commentary_dialog_layout)
                        .setPositiveButton(R.string.dialog_positive_btn, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mCommentary = mCommentaryInput.getText().toString();
                                mSmiley = mMoodPager.getCurrentItem();
                                clearCommentaryPref = mCommentaryInput.getText().length() == 0; // Clear commentary if it is empty
                            }
                        })
                        .setNegativeButton(R.string.dialog_negative_btn, null)
                        .create();
                // Automatically show the keyboard when the dialog appears
                commentaryDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                commentaryDialog.show();

                // Initialize the EditText
                mCommentaryInput = commentaryDialog.findViewById(R.id.commentary_input);
                if (mCommentary != null && !clearCommentaryPref) {
                    mCommentaryInput.setText(mCommentary);
                    mCommentaryInput.setSelection(mCommentary.length());
                }
                break;
            case R.id.history_ic:
                // Start the HistoryActivity
                Toast.makeText(MainActivity.this, "Start activity", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, HistoryActivity.class));
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Save the mood of the current day
//        mPreferences.edit().putInt(PREF_KEY_SMILEY, mMoodPager.getCurrentItem()).apply();
//        mPreferences.edit().remove(PREF_KEY_COMMENTARY).apply();
//        if (mCommentary != null && !clearCommentaryPref)
//            mPreferences.edit().putString(PREF_KEY_COMMENTARY, mCommentary).apply();

        mStorage.getMoodList().set(0, new Mood(mMoodPager.getCurrentItem(), !clearCommentaryPref ? mCommentary : null));
        mPreferences.edit().putString(PREF_KEY_MOOD_ + 0, mStorage.getMoodList().get(0).toString()).apply();
    }

    // ---------------------------------------     PRIVATE METHODS     --------------------------------------

    /**
     * Loads the mood of the current day. If the mood is not defined yet, the default values are used instead.
     */
    private void initMoodFromPrefs() {
        mPreferences = getPreferences(MODE_PRIVATE);
//        mSmiley = mPreferences.getInt(PREF_KEY_SMILEY, MOOD_DEFAULT);
//        mCommentary = mPreferences.getString(PREF_KEY_COMMENTARY, null);

        int moodIndex = 0;
        List<String> moodHistory = new ArrayList<>();
        String oneMood;
        while ((oneMood = mPreferences.getString(PREF_KEY_MOOD_ + moodIndex++, null)) != null)
            moodHistory.add(oneMood);

        mStorage = new Storage();
        mStorage.initMoodList(moodHistory);

        Mood currentMood = mStorage.getMoodList().get(0);
        if (currentMood.getSmiley() == MOOD_EMPTY)
            currentMood.setSmiley(MOOD_DEFAULT);
        mSmiley = currentMood.getSmiley();
        mCommentary = currentMood.getCommentary();
    }

    /**
     * Initializes the array from the saddest smiley to the happiest one.
     * @see #initColours()
     */
    private void initDrawables() {
        MOOD_DRAWABLES[0] = R.drawable.smiley_sad;
        MOOD_DRAWABLES[1] = R.drawable.smiley_disappointed;
        MOOD_DRAWABLES[2] = R.drawable.smiley_normal;
        MOOD_DRAWABLES[3] = R.drawable.smiley_happy;
        MOOD_DRAWABLES[4] = R.drawable.smiley_super_happy;
    }

    /**
     * Initializes the array from the "saddest" color to the "happiest" one.
     * @see #initDrawables()
     */
    private void initColours() {
        MOOD_COLOURS[0] = R.color.faded_red;
        MOOD_COLOURS[1] = R.color.warm_grey;
        MOOD_COLOURS[2] = R.color.cornflower_blue_65;
        MOOD_COLOURS[3] = R.color.light_sage;
        MOOD_COLOURS[4] = R.color.banana_yellow;
    }

    /**
     * Initializes the ViewPager.
     */
    private void initMoodPager() {
        mMoodPager = findViewById(R.id.mood_pager);
        mMoodPager.setAdapter(new MoodPagerAdapter(getSupportFragmentManager()));
        mMoodPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mMoodPager.setBackgroundResource(MOOD_COLOURS[position]);
                clearCommentaryPref = mMoodPager.getCurrentItem() != mSmiley;
            }
        });
        mMoodPager.setCurrentItem(mSmiley);
    }

    /**
     * Initializes the alarm to update the preferences at a given time.
     */
    private void schedulePrefUpdate() {
        // The preferences must be updated at midnight
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis()); // See if mandatory
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);

        // Set the class that will handle the actions to perform
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 0, new Intent(this, PrefUpdateReceiver.class), 0);

        // Set the alarm to perform the tasks at midnight and repeat it every day
        ((AlarmManager) getSystemService(Context.ALARM_SERVICE)).setInexactRepeating(AlarmManager.RTC_WAKEUP,
                                                                                    calendar.getTimeInMillis(),
                                                                                    AlarmManager.INTERVAL_DAY,
                                                                                    broadcast);
    }
}
