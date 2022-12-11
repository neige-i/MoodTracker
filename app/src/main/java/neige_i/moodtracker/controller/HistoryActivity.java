package neige_i.moodtracker.controller;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import neige_i.moodtracker.R;
import neige_i.moodtracker.model.Mood;

import static neige_i.moodtracker.controller.MainActivity.MOOD_COLORS;
import static neige_i.moodtracker.controller.MainActivity.PREF_FILE_NAME;
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

        initHistoryLayout();
    }

    // ---------------------------------------     PRIVATE METHODS     --------------------------------------

    /**
     * Retrieves the moods of the history from the preferences.
     * @throws IndexOutOfBoundsException if there are too many moods that are stored in preferences.
     */
    private void initHistoryFromPrefs() {
        SharedPreferences preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);

        mMoodHistory = new ArrayList<>();

        int i = 1; // Caution: the PREF_KEY_MOOD_0 corresponds to the current day mood. This variable must begin at 1
        String moodString;
        while ((moodString = preferences.getString(PREF_KEY_MOOD + (i++), null)) != null) {
            if (i > DAY_COUNT + 1)
                throw new IndexOutOfBoundsException("There are too many moods that are stored in preferences");
            mMoodHistory.add(Mood.fromString(moodString));
        }
    }

    /**
     * Initializes the history layout that contains the TextView and the LinearLayout.
     */
    private void initHistoryLayout() {
        LinearLayout historyLayout = findViewById(R.id.history_layout);

        if (!mMoodHistory.isEmpty()) {
            // The history can contain a maximum of DAY_COUNT moods
            // So, setting the historyLayout's weights sum to DAY_COUNT and its children to 1
            // will assure that each mood of the history has the appropriate height
            historyLayout.setWeightSum(DAY_COUNT);
            for (int i = mMoodHistory.size() - 1; i >= 0; i--)
                historyLayout.addView(getSingleMoodLayout(historyLayout, mMoodHistory.get(i), i));
        } else {
            // If the history is empty, display the TextView message
            historyLayout.setVisibility(View.GONE);
            findViewById(R.id.empty_history_txt).setVisibility(View.VISIBLE);
        }
    }

    /**
     * Creates a layout for one mood of the history.
     * @param parent the parent of the layout to create.
     * @param mood the mood that will determine the content of the layout to create.
     * @param whichDay the day which the mood was saved in.
     * @return a layout that represents one mood of the history.
     */
    private View getSingleMoodLayout(final ViewGroup parent, final Mood mood, int whichDay) {
        // ---------------------------   Set the main layout   --------------------------
        LinearLayout mainLayout = (LinearLayout) LayoutInflater.from(this)
                .inflate(R.layout.single_mood_history, parent, false);

        // mainLayout has only one direct child, which width is proportional to the smiley value
        // So, setting the mainLayout's weights sum to MOOD_COUNT and its child to (smiley value + 1)
        // will ensure that each mood of the history has the appropriate width
        // (REMEMBER the smiley value is between 0 inclusive and MOOD_COUNT exclusive, hence the '+1')
        mainLayout.setWeightSum(MOOD_COUNT);

        // -------------   Set the mood layout and the "no mood" TextView   -------------
        View moodLayout = mainLayout.findViewById(R.id.single_mood_lyt);
        int weight;
        if (mood.getSmiley() != MOOD_EMPTY) {
            // If the mood is not empty, set the correct background and a proportional width
            moodLayout.setBackgroundResource(MOOD_COLORS[mood.getSmiley()]);
            weight = mood.getSmiley() + 1;
        } else {
            // If the mood is empty, make the "no mood" TextView visible and set a maximal width
            mainLayout.findViewById(R.id.no_mood_txt).setVisibility(View.VISIBLE);
            weight = MOOD_COUNT;
        }
        moodLayout.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, weight));

        // --------------------   Set the TextView showing the day   --------------------
        ((TextView) mainLayout.findViewById(R.id.day_txt))
                .setText(getResources().getStringArray(R.array.mood_text_array)[whichDay]);

        // ----------------------------   Set the ImageView   ---------------------------
        ImageView commentaryIcon = mainLayout.findViewById(R.id.commentary_ic);
        if (!mood.getCommentary().isEmpty()) { // If there is a commentary to display
            commentaryIcon.setVisibility(View.VISIBLE);
            commentaryIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textView = (TextView) getLayoutInflater().inflate(R.layout.toast_message, parent, false);
                    textView.setText(mood.getCommentary());

                    Toast toast = new Toast(HistoryActivity.this);
                    toast.setView(textView);
                    toast.show();
                }
            });
        }

        return mainLayout;
    }
}
