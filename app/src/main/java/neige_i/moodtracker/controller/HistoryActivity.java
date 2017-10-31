package neige_i.moodtracker.controller;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import neige_i.moodtracker.R;
import neige_i.moodtracker.model.History;
import neige_i.moodtracker.model.Mood;

import static neige_i.moodtracker.controller.MainActivity.MOOD_COLORS;
import static neige_i.moodtracker.controller.MainActivity.PREF_KEY_MOOD;
import static neige_i.moodtracker.model.History.DAY_COUNT;
import static neige_i.moodtracker.model.Mood.MOOD_COUNT;
import static neige_i.moodtracker.model.Mood.MOOD_EMPTY;

public class HistoryActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private History mHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        initHistoryFromPrefs();

        initMainLayout();
    }

    private void initHistoryFromPrefs() {
        mPreferences = getSharedPreferences("controller.MainActivity", MODE_PRIVATE);
        mHistory = new History();

        List<String> moodHistory = new ArrayList<>();
        // The history and the current mood share the same key, only the current mood index is 0 and the history index begins at 1
        int i = 1;
        String oneMood;
        while ((oneMood = mPreferences.getString(PREF_KEY_MOOD + (i++), null)) != null) {
            moodHistory.add(oneMood);
        }
        mHistory.initHistory(moodHistory);
    }

    private void initMainLayout() {
        LinearLayout historyLayout = findViewById(R.id.history_layout);
        historyLayout.setWeightSum(DAY_COUNT);

        List<Mood> moodList = mHistory.getMoodList();
        if (!moodList.isEmpty()) {
            for (int i = moodList.size() - 1; i >= 0; i--) {
                Mood oneMood = moodList.get(i);
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
