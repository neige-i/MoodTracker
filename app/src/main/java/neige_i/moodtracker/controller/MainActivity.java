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

/**
 * This activity retrieves the mood of the current day from the preferences (if it exits).
 * It initializes the UI to show the correct mood.
 * It intercepts click events on ImageViews and performs appropriate actions:
 *      - open a Dialog to see or modify the commentary,
 *      - start a new Activity to show the history,
 *      - open a Dialog to start a new Activity in another app to share the current mood.
 * It also schedule a task that update preferences at a specified time.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    // -------------------------------------     INSTANCE VARIABLES     -------------------------------------

    /**
     * Vertical ViewPager that allows the user to swipe between the different smileys.
     */
    private VerticalViewPager mMoodPager;
    /**
     * EditText, displayed in the Dialog, that allows the user to put a commentary.
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
     * <p>Control variable to determinate if the commentary is correct.<br />
     * As the current mood is not saved at each ViewPager swipe, the commentary must be checked at two moments:
     *      hen displaying it in the EditText,
     *      when saving it into preferences.</p>
     * <p>The situation is as follows: the user enters a commentary for a specified mood, and then swipes to another one.</p>
     * <p>First case: the user opens the Dialog.
     * It would be inappropriate if he can still see his commentary in the EditText.
     * Therefore the EditText must not contain the commentary.</p>
     * <p>Second case: the user exits the app without putting a new commentary.
     * The current one is still in memory and must not be taken into consideration.
     * Therefore the value to put into preferences is an empty String instead of the current commentary.</p>
     */
    private boolean isCommentaryCorrect;

    // ---------------------------------------     CLASS VARIABLES     --------------------------------------

    /**
     * Array containing the drawable IDs of the different smileys.<br />
     * Each drawable has a specific background color.
     * @see #MOOD_COLORS
     */
    public static final int[] MOOD_DRAWABLES = new int[MOOD_COUNT];
    /**
     * Array containing the color IDs of the different backgrounds.<br />
     * Each color is the background of a specific drawable.
     * @see #MOOD_DRAWABLES
     */
    public static final int[] MOOD_COLORS = new int[MOOD_COUNT];
    /**
     * Constant for storing the mood in the preferences.
     */
    public static final String PREF_KEY_MOOD = "PREF_KEY_MOOD_";
    /**
     * Name of the preferences file where the data is saved.
     */
    public static final String PREF_FILE_NAME = "mood";

    // -------------------------------------     OVERRIDDEN METHODS     -------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDrawables();
        initColours();

        initMoodFromPrefs();

        initMoodPager();
        findViewById(R.id.new_note_ic).setOnClickListener(this);
        findViewById(R.id.history_ic).setOnClickListener(this);
        findViewById(R.id.share_ic).setOnClickListener(this);

        schedulePrefUpdate();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_note_ic:
                openCommentaryDialog();
                break;
            case R.id.history_ic:
                startActivity(new Intent(this, HistoryActivity.class));
                break;
            case R.id.share_ic:
                shareCurrentMood();
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveMoodToPrefs();
    }

    // ---------------------------------------     PRIVATE METHODS     --------------------------------------

    /**
     * Retrieves the mood of the current day from the preferences.
     * @see #saveMoodToPrefs()
     */
    private void initMoodFromPrefs() {
        mPreferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);

        // Initialize the current mood with the preferences
        // If no preferences is found, then initialize with an empty mood
        mCurrentMood = Mood.fromString(mPreferences.getString(PREF_KEY_MOOD + 0, new Mood().toString()));

        // If the current mood is empty, then set it to default
        if (mCurrentMood.getSmiley() == MOOD_EMPTY)
            mCurrentMood.setSmiley(MOOD_DEFAULT);
    }

    /**
     * Save the mood of the current day into preferences.
     * @see #initMoodFromPrefs()
     */
    private void saveMoodToPrefs() {
        mCurrentMood.setSmiley(mMoodPager.getCurrentItem());
        if (!isCommentaryCorrect)
            mCurrentMood.setCommentary(""); // Reset the commentary if incorrect

        mPreferences.edit().putString(PREF_KEY_MOOD + 0, mCurrentMood.toString()).apply();
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
        isCommentaryCorrect = true;
        mMoodPager = findViewById(R.id.mood_pager);
        mMoodPager.setAdapter(new MoodPagerAdapter(getSupportFragmentManager()));
        mMoodPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // Update the background and the control variable at each page swipe
                mMoodPager.setBackgroundResource(MOOD_COLORS[position]);
                isCommentaryCorrect = mMoodPager.getCurrentItem() == mCurrentMood.getSmiley();
            }
        });
        mMoodPager.setCurrentItem(mCurrentMood.getSmiley()); // Initialize the ViewPager's current item
    }

    /**
     * Opens a Dialog which contains an EditText to put a commentary.
     */
    private void openCommentaryDialog() {
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
        assert commentaryDialog.getWindow() != null; // To remove the warning at the next instruction
        commentaryDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        commentaryDialog.show();

        // Initialize the EditText
        mCommentaryInput = commentaryDialog.findViewById(R.id.commentary_input);
        if (isCommentaryCorrect) {
            mCommentaryInput.setText(mCurrentMood.getCommentary());
            mCommentaryInput.setSelection(mCurrentMood.getCommentary().length());
        }
    }

    /**
     * Share the mood of the current day with other apps.
     */
    private void shareCurrentMood() {
        saveMoodToPrefs();

        String[] smileyTab = { ": (", ": /", ": |", ": )", ": D" };
        String textToSend = smileyTab[mCurrentMood.getSmiley()] + "\n" + mCurrentMood.getCommentary() + "\n" +
                "------------------------------" + "\n" + getString(R.string.share_text);

        Intent shareIntent = new Intent(Intent.ACTION_SEND); // Implicit intent
        shareIntent.putExtra(Intent.EXTRA_TEXT, textToSend);
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_title)));
    }

    /**
     * Initializes an alarm to update the preferences at a given time.
     */
    private void schedulePrefUpdate() {
        // The alarm is correctly triggered at midnight, every day
        // But it is also triggered at each app launch, which is not desired
        // To prevent this, the actions in this method are only performed at the first app launch
        if (mPreferences.getString(PREF_KEY_MOOD + 0, null) == null) {
            // The preferences must be updated at midnight
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);

            // Set the class that will handle the actions to perform
            PendingIntent broadcast = PendingIntent.getBroadcast(this, 0, new Intent(this, PrefUpdateReceiver.class), 0);

            // Set the alarm to perform the tasks at midnight and repeat it every day
            AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            assert alarm != null; // To remove the warning at the next instruction
            alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, broadcast);
        }
    }
}
