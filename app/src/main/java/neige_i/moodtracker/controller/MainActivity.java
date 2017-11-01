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

import java.util.Calendar;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;
import neige_i.moodtracker.R;
import neige_i.moodtracker.model.Mood;
import neige_i.moodtracker.model.MoodPagerAdapter;
import neige_i.moodtracker.model.PrefUpdateReceiver;

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
     * Mood of the current day.
     */
    private Mood mCurrentMood;
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
    private boolean isCommentaryCorrect;

    // ---------------------------------------     CLASS VARIABLES     --------------------------------------

    /**
     * Array containing the drawables of the different smileys.<br />
     * Each drawable has a specific background color.
     * @see #MOOD_COLORS
     */
    public static final int[] MOOD_DRAWABLES = new int[MOOD_COUNT];
    /**
     * Array containing the colors of the different backgrounds.<br />
     * Each color is the background for a specific drawable.
     * @see #MOOD_DRAWABLES
     */
    public static final int[] MOOD_COLORS = new int[MOOD_COUNT];
    /**
     * Constant for storing the mood in the preferences.
     */
    public static final String PREF_KEY_MOOD = "PREF_KEY_MOOD_";

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
        findViewById(R.id.share_ic).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_note_ic:
                AlertDialog commentaryDialog = new AlertDialog.Builder(this)
                        .setView(R.layout.dialog_commentary)
                        .setPositiveButton(R.string.dialog_positive_btn, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mCurrentMood.setCommentary(mCommentaryInput.getText().toString());
                                mCurrentMood.setSmiley(mMoodPager.getCurrentItem());
                                isCommentaryCorrect = true;
                            }
                        })
                        .setNegativeButton(R.string.dialog_negative_btn, null)
                        .create();

                // Automatically show the keyboard when the dialog appears
                commentaryDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                commentaryDialog.show();

                // Initialize the EditText
                mCommentaryInput = commentaryDialog.findViewById(R.id.commentary_input);
                if (isCommentaryCorrect) {
                    mCommentaryInput.setText(mCurrentMood.getCommentary());
                    mCommentaryInput.setSelection(mCurrentMood.getCommentary().length());
                }
                break;
            case R.id.history_ic:
                startActivity(new Intent(this, HistoryActivity.class));
                break;
            case R.id.share_ic:
                String[] smileyTab = { ": (", ": /", ": |", ": )", ": D" };
                String textToSend = smileyTab[mCurrentMood.getSmiley()] + "\n" + mCurrentMood.getCommentary() + "\n" +
                        "------------------------------" + "\n" + getString(R.string.share_text);
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, textToSend);
                shareIntent.setType("text/plain");
                startActivity(Intent.createChooser(shareIntent, getString(R.string.share_title)));
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        mCurrentMood.setSmiley(mMoodPager.getCurrentItem());
        if (!isCommentaryCorrect)
            mCurrentMood.setCommentary(""); // Reset the commentary if incorrect

        mPreferences.edit().putString(PREF_KEY_MOOD + 0, mCurrentMood.toString()).apply();
    }

    // ---------------------------------------     PRIVATE METHODS     --------------------------------------

    /**
     * Retrieves the mood of the current day from the preferences.
     */
    private void initMoodFromPrefs() {
        mPreferences = getPreferences(MODE_PRIVATE);

        // Initialize the current mood with the preferences
        // If no preferences is found, then initialize with an empty mood
        mCurrentMood = Mood.fromString(mPreferences.getString(PREF_KEY_MOOD + 0, new Mood().toString()));

        // If the current mood is empty, then set it to default
        if (mCurrentMood.getSmiley() == MOOD_EMPTY)
            mCurrentMood.setSmiley(MOOD_DEFAULT);
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
        MOOD_COLORS[0] = R.color.faded_red;
        MOOD_COLORS[1] = R.color.warm_grey;
        MOOD_COLORS[2] = R.color.cornflower_blue_65;
        MOOD_COLORS[3] = R.color.light_sage;
        MOOD_COLORS[4] = R.color.banana_yellow;
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
                mMoodPager.setBackgroundResource(MOOD_COLORS[position]);
                isCommentaryCorrect = mMoodPager.getCurrentItem() == mCurrentMood.getSmiley();
            }
        });
        mMoodPager.setCurrentItem(mCurrentMood.getSmiley());
        // Important initialization: isCommentaryCorrect is only updated at changing page
        // But if the first shown mood is the #0, the page does not change and isCommentaryCorrect is not updated
        // That is why, isCommentaryCorrect must be set to true,
        // in order to show the commentary in the EditText when the start mood is #0
        isCommentaryCorrect = true;
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
