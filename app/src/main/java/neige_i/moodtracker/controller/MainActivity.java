package neige_i.moodtracker.controller;

import static neige_i.moodtracker.model.Mood.MOOD_COUNT;
import static neige_i.moodtracker.model.Mood.MOOD_DEFAULT;
import static neige_i.moodtracker.model.Mood.MOOD_EMPTY;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import java.util.Calendar;

import dagger.hilt.android.AndroidEntryPoint;
import neige_i.moodtracker.R;
import neige_i.moodtracker.model.Mood;
import neige_i.moodtracker.model.PrefUpdateReceiver;
import neige_i.moodtracker.ui.comment.MoodCommentDialogFragment;
import neige_i.moodtracker.ui.emoji.EmojiPagerAdapter;

/**
 * This activity retrieves the mood of the current day from the preferences (if it exits).
 * It initializes the UI to show the correct mood.
 * It intercepts click events on ImageViews and performs appropriate actions:
 *      - open a Dialog to see or modify the commentary,
 *      - start a new Activity to show the history,
 *      - open a Dialog to start a new Activity in another app to share the current mood.
 * It also schedule a task that update preferences at a specified time.
 */
@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    // -------------------------------------     INSTANCE VARIABLES     -------------------------------------

    private ViewPager2 emojiPager;
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

    private MainViewModel viewModel;

    // ---------------------------------------     CLASS VARIABLES     --------------------------------------

/**
     * Array containing the color IDs of the different backgrounds.<br />
     * Each color is the background of a specific drawable.
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

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.getPagerUiLiveData().observe(this, this::updateMoodPager);

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
        if (v.getId() == R.id.new_note_ic) {
            openCommentaryDialog();
        } else if (v.getId() == R.id.history_ic) {
            startActivity(new Intent(this, HistoryActivity.class));
        } else if (v.getId() == R.id.share_ic) {
            shareCurrentMood();
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
        mCurrentMood.setSmiley(emojiPager.getCurrentItem());
        if (!isCommentaryCorrect)
            mCurrentMood.setCommentary(""); // Reset the commentary if incorrect

        mPreferences.edit().putString(PREF_KEY_MOOD + 0, mCurrentMood.toString()).apply();
    }

    /**
     * Initializes the array from the "saddest" color to the "happiest" one.
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
        emojiPager = findViewById(R.id.mood_pager);
        emojiPager.setAdapter(new EmojiPagerAdapter(this));
        emojiPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                // Update the control variable at each page swipe
                isCommentaryCorrect = emojiPager.getCurrentItem() == mCurrentMood.getSmiley();
                viewModel.onPagerPositionChanged(position);
            }
        });
    }

    private void updateMoodPager(MoodPagerUi moodPagerUi) {
        emojiPager.setCurrentItem(moodPagerUi.getPosition());
        emojiPager.setBackgroundResource(moodPagerUi.getBackgroundColor());
    }

    /**
     * Opens a Dialog which contains an EditText to put a commentary.
     */
    private void openCommentaryDialog() {
        new MoodCommentDialogFragment().show(getSupportFragmentManager(), null);
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
            PendingIntent broadcast = PendingIntent.getBroadcast(this, 0, new Intent(this, PrefUpdateReceiver.class), PendingIntent.FLAG_IMMUTABLE);

            // Set the alarm to perform the tasks at midnight and repeat it every day
            AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            assert alarm != null; // To remove the warning at the next instruction
            alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, broadcast);
        }
    }
}
