package neige_i.moodtracker.controller;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import neige_i.moodtracker.R;
import neige_i.moodtracker.model.Mood;

import static neige_i.moodtracker.controller.MainActivity.MOOD_COLORS;
import static neige_i.moodtracker.controller.MainActivity.PREF_KEY_MOOD;
import static neige_i.moodtracker.model.Mood.MOOD_COUNT;
import static neige_i.moodtracker.model.Mood.MOOD_EMPTY;

public class HistoryActivity extends AppCompatActivity {
    // -------------------------------------     INSTANCE VARIABLES     -------------------------------------

    /**
     * List of moods that are saved in the history.
     * The moods are sorted from the newest to the oldest.
     */
    private List<Mood> mMoodHistory;

    // ---------------------------------------     CLASS VARIABLES     --------------------------------------

    /**
     * Number of moods that will be saved in history.
     */
    public static final int DAY_COUNT = 7;

    // -------------------------------------     OVERRIDDEN METHODS     -------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        initHistoryFromPrefs();

        initMainLayout();
    }

    // ---------------------------------------     PRIVATE METHODS     --------------------------------------

    private void initHistoryFromPrefs() {
        SharedPreferences preferences = getSharedPreferences("controller.MainActivity", MODE_PRIVATE);

        mMoodHistory = new ArrayList<>();
        // Caution: the PREF_KEY_MOOD_0 corresponds to the current day mood. This variable must begin at 1
        int i = 1;
        String oneMood;
        while ((oneMood = preferences.getString(PREF_KEY_MOOD + (i++), null)) != null) {
            if (i > DAY_COUNT + 1)
                throw new IllegalArgumentException("There are too many moods that are stored in prefs");
            mMoodHistory.add(Mood.fromString(oneMood));
        }
    }

    private void initMainLayout() {
        LinearLayout historyLayout = findViewById(R.id.history_layout);
        historyLayout.setWeightSum(DAY_COUNT);

        if (!mMoodHistory.isEmpty()) {
            for (int i = mMoodHistory.size() - 1; i >= 0; i--) {
                Mood oneMood = mMoodHistory.get(i);
                LinearLayout oneMoodLayout = (LinearLayout) LayoutInflater.from(this)
                        .inflate(R.layout.single_mood_history, historyLayout, false);
                oneMoodLayout.setWeightSum(MOOD_COUNT);

                ((TextView) oneMoodLayout.findViewById(R.id.day_txt))
                        .setText(getResources().getStringArray(R.array.mood_text_array)[i]);

                View globalLayout = oneMoodLayout.findViewById(R.id.single_mood_lyt);
                int weight;
                if (oneMood.getSmiley() != MOOD_EMPTY) {
                    globalLayout.setBackgroundResource(MOOD_COLORS[oneMood.getSmiley()]);
                    weight = oneMood.getSmiley() + 1;
                } else {
                    oneMoodLayout.findViewById(R.id.no_mood_txt).setVisibility(View.VISIBLE);
                    weight = MOOD_COUNT;
                }
                globalLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.MATCH_PARENT, weight));

                if (!oneMood.getCommentary().isEmpty())
                    oneMoodLayout.findViewById(R.id.commentary_ic).setVisibility(View.VISIBLE);

                historyLayout.addView(oneMoodLayout);
            }
        } else {
            historyLayout.setVisibility(View.GONE);
            findViewById(R.id.empty_history_txt).setVisibility(View.VISIBLE);
        }
    }
}
