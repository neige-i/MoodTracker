package neige_i.moodtracker.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import neige_i.moodtracker.R;

import static neige_i.moodtracker.model.Storage.DAY_COUNT;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        LinearLayout historyLayout = findViewById(R.id.history_layout);
        for (int i = DAY_COUNT - 2; i >= 0; i--) {
            View view = LayoutInflater.from(this).inflate(R.layout.single_history_mood, historyLayout, false);
            TextView textView = view.findViewById(R.id.day_txt);
            textView.setText(getResources().getStringArray(R.array.mood_text_array)[i]);
            historyLayout.addView(view);
        }
    }
}
